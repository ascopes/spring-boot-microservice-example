package io.ascopes.springboot.usermgmt.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;


@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserResponse extends RepresentationModel<UserResponse> {
    @NonNull
    private final String id;

    @NonNull
    private final String userName;

    @NonNull
    private final String email;

    @JsonSerialize(using = ToStringSerializer.class)
    @NonNull
    private final LocalDateTime createdAt;

    @NonNull
    private final String createdBy;

    @JsonSerialize(using = ToStringSerializer.class)
    @Nullable
    private final LocalDateTime lastModifiedAt;

    @Nullable
    private final String lastModifiedBy;
}
