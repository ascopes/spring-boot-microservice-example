package io.ascopes.springboot.microserviceexample.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * {@code @EqualsAndHashCode} generates an {@code equals} and {@code hashCode} override during compilation.
 * <p>
 * {@code @Getter} generates public {@code getX} methods for all fields.
 * <p>
 * {@code @ToString} generates a human-readable {@code toString} override.
 *
 * If I wanted to have a setter for the message, I could add the {@code @Setter} annotation, or just remove all of
 * those annotations and use {@code @Data} instead, which would add all of these for me implicitly to make a full
 * POJO dataclass.
 */
@EqualsAndHashCode
@Getter
@ToString
public class PingResponseBody {
    private final String message = "pong!";
}
