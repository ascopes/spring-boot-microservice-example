package io.ascopes.springboot.usermgmt.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.math.BigInteger;

@Documented
@Constraint(validatedBy = ValidUserId.UserIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER, ElementType.TYPE_USE, ElementType.PARAMETER})
public @interface ValidUserId {
    String message() default "{io.ascopes.springboot.usermgmt.validation.ValidUserId.message}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

    class UserIdValidator implements ConstraintValidator<ValidUserId, String> {
        private static final BigInteger MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

        @Override
        public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
            return field == null
                || field.chars().allMatch(Character::isDigit) && !new BigInteger(field).min(MAX_VALUE).equals(MAX_VALUE);
        }
    }
}
