package io.ascopes.springboot.usermgmt.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InvalidParam {
    private final String attribute;
    private final String reason;
    private final String code;
    private final String location;
    private final Object rejectedValue;
}
