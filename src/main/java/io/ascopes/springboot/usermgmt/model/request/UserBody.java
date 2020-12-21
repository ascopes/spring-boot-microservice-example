package io.ascopes.springboot.usermgmt.model.request;

import io.ascopes.springboot.usermgmt.validation.Groups;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
