package io.ascopes.springboot.usermgmt.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.math.BigInteger;

/**
 * Validate an unsigned String user ID.
 * <p>
 * This is handled as a string to allow us to provide a defacto 400 validation failure response
 * if the parameter is not a numeric value.
 */
@Documented
@Constraint(validatedBy = UserId.UserIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.PARAMETER})
public @interface UserId {
    String message() default "{io.ascopes.springboot.usermgmt.validation.ValidUserId.message}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    class UserIdValidator implements ConstraintValidator<UserId, String> {
        private static final BigInteger MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

        @Override
        public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
            return field == null
                || field.chars().allMatch(Character::isDigit) && !new BigInteger(field).min(MAX_VALUE).equals(MAX_VALUE);
        }
    }
}
