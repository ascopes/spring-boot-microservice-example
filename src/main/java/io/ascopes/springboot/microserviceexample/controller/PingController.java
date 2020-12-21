package io.ascopes.springboot.microserviceexample.controller;

import io.ascopes.springboot.microserviceexample.model.PingResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The controller for the {@code /ping} endpoint.
 * <p>
 * This is annotated with {@code @RestController} to tell Spring to create an instance of this class
 * on startup. {@code @Slf4j} is a Lombok annotation that injects a named SLF4J (Simple Logging Facade For Java)
 * logger into this class as a field called {@code log}.
 * <p>
 * We could annotate this class with {@code @RequestMapping("/foo/bar")} to put all endpoints in this class on the
 * {@code /foo/bar} route. If we did this, the {@code GET /ping} endpoint would become {@code GET /foo/bar/ping}
 * instead.
 * <p>
 * We could also override the context path for the entire application in the {@code application.yml} resource if
 * we wanted to.
 */
@RestController
@Slf4j
public class PingController {
    /**
     * We can autowire parameters into this endpoint simply by adding them. For example, if I defined a parameter
     * {@code @RequestParam("name") String name}, then the value of a query string with the identifier {@code name}
     * would be injected.
     */
    @GetMapping("/ping")
    public ResponseEntity<PingResponseBody> ping() {
        log.info("Ping!");
        return ResponseEntity.ok(new PingResponseBody());
    }
}
