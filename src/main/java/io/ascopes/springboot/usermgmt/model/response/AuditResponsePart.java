package io.ascopes.springboot.usermgmt.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditResponsePart {
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
