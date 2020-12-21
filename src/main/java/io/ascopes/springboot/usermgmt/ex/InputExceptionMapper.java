package io.ascopes.springboot.usermgmt.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.ascopes.springboot.usermgmt.model.response.InvalidParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@ControllerAdvice
@Slf4j
public class InputExceptionMapper {
    private final DefaultErrorAttributes errorAttributes;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
                                                                        WebRequest webRequest) {
        log.debug("http message was unreadable", exception);
        val location = exception.getCause() instanceof JsonProcessingException
            ? "body"
            : "request";

        val invalidParam = Stream.of(new InvalidParam(
            null, "message was not parsable", null, location, null
        ));
        return createResponse(
            invalidParam,
            webRequest,
            "message was not parsable"
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception,
                                                                     WebRequest webRequest) {
        log.debug("general constraint violation", exception);
        return createResponse(
            exception
                .getConstraintViolations()
                .stream()
                .map(c -> new InvalidParam(
                    c.getPropertyPath().toString(),
                    c.getMessage(),
                    c.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
                    "request",
                    c.getInvalidValue()
                )),
            webRequest,
            "validation errors occurred on the request"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                        WebRequest webRequest) {
        log.debug("http method argument was not valid", exception);
        return createResponse(
            exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new InvalidParam(
                    f.getObjectName() + "." + f.getField(),
                    f.getDefaultMessage(),
                    f.getCode(),
                    "body",
                    f.getRejectedValue()
                )),
            webRequest,
            "validation errors occurred on the request body"
        );
    }

    private ResponseEntity<Object> createResponse(Stream<InvalidParam> invalidParams, WebRequest webRequest, String message) {
        val attributes = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        attributes.put("status", BAD_REQUEST.value());
        attributes.put("error", BAD_REQUEST.getReasonPhrase());
        attributes.put("message", message);

        val params = invalidParams.collect(Collectors.toList());

        if (!params.isEmpty()) {
            attributes.put("invalidParams", params);
        }

        return ResponseEntity.badRequest().body(attributes);
    }
}
