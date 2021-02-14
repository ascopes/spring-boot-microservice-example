package io.ascopes.springboot.usermgmt.mapper;

import io.ascopes.springboot.usermgmt.model.entity.UserEntity;
import io.ascopes.springboot.usermgmt.model.request.UserBody;
import io.ascopes.springboot.usermgmt.model.response.UserResponse;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Object mapper for user entities.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    @NonNull
    @Mapping(target = "userId", ignore = true)
    UserEntity userToUserEntity(@NonNull UserBody userBody);

    @NonNull
    @Mapping(source = "userId", target = "id")
    UserResponse userEntityToUserResponse(@NonNull UserEntity userEntity);

    default LocalDateTime longToLocalDateTime(long value) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC);
    }
}
