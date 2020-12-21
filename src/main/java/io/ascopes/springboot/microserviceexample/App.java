package io.ascopes.springboot.microserviceexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entrypoint. Simply tells Spring to start an application. The {@code @SpringBootApplication} tells
 * Spring to configure this as a Spring Boot application which adds a large number of useful things in for you including
 * the web framework configuration.
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
