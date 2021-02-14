package io.ascopes.springboot.usermgmt.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Invalid parameter description.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InvalidParam {
    /**
     * The name of the invalid attribute.
     */
    private final String attribute;

    /**
     * Why it is invalid.
     */
    private final String reason;

    /**
     * The error code.
     */
    private final String code;

    /**
     * The error location.
     */
    private final String location;

    /**
     * The rejected value.
     */
    private final Object rejectedValue;
}
