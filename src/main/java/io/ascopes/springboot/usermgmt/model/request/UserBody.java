package io.ascopes.springboot.usermgmt.model.request;

import io.ascopes.springboot.usermgmt.validation.Groups;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The body of a request that operates on a user.
 */
@Data
@Validated
public class UserBody {
    @NotNull(groups = Groups.OnCreateOnly.class)
    @Pattern(regexp = "^[A-Za-z0-9-_]+$")
    @Size(min = 3, max = 25)
    private String userName;

    @NotNull(groups = Groups.OnCreateOnly.class)
    @Email
    private String email;
}
