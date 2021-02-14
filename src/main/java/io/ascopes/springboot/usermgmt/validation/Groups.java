package io.ascopes.springboot.usermgmt.validation;

import lombok.experimental.UtilityClass;

/**
 * Validation groups. These can be used to filter validation rules
 * on objects to specific use cases only.
 */
@UtilityClass
public class Groups {
    /**
     * Group to indicate a validation rule should only be applied during creation.
     */
    public interface OnCreateOnly {
    }
}
