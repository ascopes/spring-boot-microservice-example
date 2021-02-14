package io.ascopes.springboot.usermgmt.controller;

import io.ascopes.springboot.usermgmt.ex.BadRequestException;
import io.ascopes.springboot.usermgmt.ex.NotFoundException;
import io.ascopes.springboot.usermgmt.mapper.UserMapper;
import io.ascopes.springboot.usermgmt.model.request.UserBody;
import io.ascopes.springboot.usermgmt.repository.UserRepository;
import io.ascopes.springboot.usermgmt.validation.Groups;
import io.ascopes.springboot.usermgmt.validation.UserId;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * User manipulation endpoints.
 */
@AllArgsConstructor
@RequestMapping(UsersController.RESOURCE)
@RestController
@Slf4j
@SuppressWarnings("unused")
@Validated
public class UsersController {
    static final String RESOURCE = "/users";

    private final ServerProperties serverProperties;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Get a user by their ID.
     * <p>
     * Throw a 404 Not Found if not found.
     *
     * @param userId the user ID to look up.
     * @return the user object in a 200 OK response.
     */
    @Operation(
        operationId = "getUser",
        summary = "Get a user by an ID",
        description = "Look up the ID in the database and return the corresponding object if it exists",
        parameters = @Parameter(name = "userId", description = "The user ID", required = true),
        responses = {
            @ApiResponse(
                description = "The user was found",
                responseCode = "200",
                links = {
                    @Link(operationId = "postUser"),
                    @Link(operationId = "putUser"),
                    @Link(operationId = "deleteUser"),
                }
                // etc...
            )
        }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@Valid @NotBlank @UserId @PathVariable String userId) {
        log.info("GET userId={}", userId);

        val resp = userRepository
            .findByStringId(userId)
            .map(userMapper::userEntityToUserResponse)
            .orElseThrow(() -> {
                log.warn("GET userId={} - user not found", userId);
                return new NotFoundException("user '%s' was not found", userId);
            });

        log.info("GET userId={} - user found", userId);

        resp.add(linkTo(methodOn(UsersController.class).getUser(userId)).withSelfRel());

        return ResponseEntity.ok(resp);
    }

    /**
     * Create a new user.
     * <p>
     * Throw a 400 Bad Request if the user already exists or is invalid.
     *
     * @param httpRequest the HTTP servlet request.
     * @param body        the new user to create.
     * @return the created user in a 201 Created response.
     */
    @Operation(operationId = "postUser")
    @PostMapping
    @Transactional
    @Validated(Groups.OnCreateOnly.class)
    public ResponseEntity<?> createUser(HttpServletRequest httpRequest,
                                        @Valid @NotNull @RequestBody(required = false) UserBody body) {
        log.info("POST new user userName={}", body.getUserName());

        userRepository
            .findByUserNameIgnoreCase(body.getUserName())
            .ifPresent(user -> {
                log.warn("DELETE userName={} - user already exists", user.getUserName());
                throw new BadRequestException("user with username '%s' already exists", user.getUserName());
            });

        val entity = userRepository.save(userMapper.userToUserEntity(body));
        val resp = userMapper.userEntityToUserResponse(entity);

        // Add HAL attributes.
        val selfLink = linkTo(methodOn(UsersController.class).getUser(entity.getUserId().toString())).withRel("user");
        resp.add(selfLink);

        log.info("created new user");

        return ResponseEntity
            .created(selfLink.toUri())
            .body(resp);
    }

    /**
     * Update a given user.
     * <p>
     * This endpoint is idempotent.
     * <p>
     * If the user is not valid, throw a 400 Bad Request. If the user does not exist,
     * throw a 404 Not Found.
     *
     * @param userId   the user ID to look up.
     * @param userBody the updated user body to use to update the entity with.
     * @return a 200 OK with the updated user object in the body.
     */
    @Operation(operationId = "putUser")
    @PutMapping("/{userId}")
    @Transactional
    public ResponseEntity<?> updateUser(@Valid @PathVariable @UserId String userId,
                                        @Valid @NotNull @RequestBody UserBody userBody) {

        log.info("PUT userId={}", userId);

        val user = userRepository
            .findByStringId(userId)
            .orElseThrow(() -> {
                log.warn("PUT userId={} - user not found", userId);
                return new NotFoundException("user with id '%s' does not exist", userId);
            });

        if (userBody.getUserName() != null) {
            user.setUserName(userBody.getUserName().toLowerCase(Locale.ENGLISH));
        }

        if (userBody.getEmail() != null) {
            user.setUserName(userBody.getEmail().toLowerCase(Locale.ENGLISH));
        }

        val resp = userMapper.userEntityToUserResponse(userRepository.save(user));

        // Add HAL attributes.
        resp.add(linkTo(methodOn(UsersController.class).getUser(userId)).withRel("user"));

        log.info("updated userId={}", userId);
        return ResponseEntity.ok(resp);
    }

    /**
     * Delete a given user.
     * <p>
     * If the user does not exist, throw a 404 Not Found.
     *
     * @param userId the user ID to look up.
     * @return a 204 No Content response.
     */
    @Operation(operationId = "deleteUser")
    @DeleteMapping("/{userId}")
    @Transactional
    public ResponseEntity<?> deleteUser(@Valid @PathVariable @UserId String userId) {
        log.info("PUT userId={}", userId);

        userRepository
            .findByStringId(userId)
            .orElseThrow(() -> {
                log.warn("DELETE userId={} - user not found", userId);
                return new NotFoundException("user with id '%s' does not exist", userId);
            });

        userRepository.deleteByStringId(userId);
        log.info("DELETE userId={} - user was deleted", userId);

        return ResponseEntity
            .noContent()
            .build();
    }
}

