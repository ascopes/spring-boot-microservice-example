package io.ascopes.springboot.usermgmt.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


@AllArgsConstructor
@Data
public class UserResponse {
    @NonNull
    private final String id;

    @NonNull
    private final String userName;

    @NonNull
    private final String email;

    @NonNull
    private final AuditResponsePart auditLog;
}
