package io.ascopes.springboot.usermgmt.controller;

import io.ascopes.springboot.usermgmt.ex.BadRequestException;
import io.ascopes.springboot.usermgmt.ex.NotFoundException;
import io.ascopes.springboot.usermgmt.model.entity.UserEntity;
import io.ascopes.springboot.usermgmt.model.request.UserBody;
import io.ascopes.springboot.usermgmt.model.response.AuditResponsePart;
import io.ascopes.springboot.usermgmt.model.response.UserResponse;
import io.ascopes.springboot.usermgmt.repository.UserRepository;
import io.ascopes.springboot.usermgmt.validation.Groups;
import io.ascopes.springboot.usermgmt.validation.ValidUserId;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;

@AllArgsConstructor
@RequestMapping(UsersController.RESOURCE)
@RestController
@Slf4j
@Validated
public class UsersController {
    static final String RESOURCE = "/users";

    private final ServerProperties serverProperties;
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@Valid @NotNull @PathVariable String userId) {
        log.info("GET userId={}", userId);

        val user = userRepository
            .findById(Long.parseLong(userId))
            .map(this::convertEntityToUser);

        if (!user.isPresent()) {
            log.warn("GET userId={} - user not found", userId);
            throw new NotFoundException("user '%s' was not found", userId);
        }

        log.info("GET userId={} - user found", userId);
        return ResponseEntity.ok(user.get());
    }

    @PostMapping
    @Transactional
    @Validated(Groups.OnCreateOnly.class)
    public ResponseEntity<?> createUser(HttpServletRequest httpRequest,
                                        @Valid @NotNull @RequestBody(required = false) UserBody body) {
        log.info("POST new user userName={}", body.getUserName());

        val existingUser = userRepository
            .findByUserNameIgnoreCase(body.getUserName());

        if (existingUser.isPresent()) {
            log.warn("DELETE userName={} - user already exists", body.getUserName());
            throw new BadRequestException("user with username '%s' already exists", body.getUserName());
        }

        var entity = new UserEntity();
        entity.setUserName(body.getUserName());
        entity.setEmail(body.getEmail());
        entity = userRepository.save(entity);

        val responseBody = convertEntityToUser(entity);

        val getUri = UriComponentsBuilder
            .newInstance()
            .scheme(httpRequest.getScheme())
            .host(httpRequest.getLocalAddr())
            .port(httpRequest.getLocalPort())
            .path(httpRequest.getContextPath() + RESOURCE)
            .pathSegment(Long.toString(entity.getUserId()))
            .build()
            .toUri();

        log.info("created new user at {}", getUri);

        return ResponseEntity
            .created(getUri)
            .body(responseBody);
    }

    @PutMapping("/{userId}")
    @Transactional
    public ResponseEntity<?> updateUser(@Valid @PathVariable @ValidUserId String userId,
                                        @Valid @NotNull @RequestBody UserBody userBody) {

        log.info("PUT userId={}", userId);

        val optionalUser = userRepository
            .findById(Long.valueOf(userId));

        if (!optionalUser.isPresent()) {
            log.warn("PUT userId={} - user not found", userId);
            throw new BadRequestException("user with id '%s' does not exist", userId);
        }

        val userEntity = optionalUser.get();

        if (userBody.getUserName() != null) {
            userEntity.setUserName(userBody.getUserName().toLowerCase(Locale.ENGLISH));
        }

        if (userBody.getEmail() != null) {
            userEntity.setUserName(userBody.getEmail().toLowerCase(Locale.ENGLISH));
        }

        val responseBody = convertEntityToUser(userRepository.save(userEntity));

        log.info("updated userId={}", userId);

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<?> deleteUser(@Valid @PathVariable @ValidUserId String userId) {
        log.info("PUT userId={}", userId);

        val rawUserId = Long.valueOf(userId);

        val optionalUser = userRepository
            .findById(rawUserId);

        if (!optionalUser.isPresent()) {
            log.warn("DELETE userId={} - user not found", userId);
            throw new NotFoundException("user with id '%s' does not exist", userId);
        }

        userRepository.deleteById(rawUserId);
        log.info("DELETE userId={} - user was deleted", userId);

        return ResponseEntity
            .noContent()
            .build();
    }

    private UserResponse convertEntityToUser(@NonNull UserEntity entity) {
        return new UserResponse(
            Long.toString(entity.getUserId()),
            entity.getUserName(),
            entity.getEmail(),
            new AuditResponsePart(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getCreatedAt()), ZoneOffset.UTC),
                entity.getCreatedBy(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(entity.getLastModifiedAt()), ZoneOffset.UTC),
                entity.getLastModifiedBy()
            )
        );
    }
}

