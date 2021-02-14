package io.ascopes.springboot.usermgmt.config;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import springfox.boot.starter.autoconfigure.SpringfoxConfigurationProperties;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;


/**
 * Configures what is exposed by Swagger.
 */
@Configuration
@EnableOpenApi
public class SpringFoxConfig {
    private static final String CONTROLLER_PACKAGE = SpringFoxConfig
        .class
        .getCanonicalName()
        .replace("config." + SpringFoxConfig.class.getSimpleName(), "controller");

    // Fix for https://github.com/springfox/springfox/issues/3730
    @Bean
    @SneakyThrows(URISyntaxException.class)
    RouterFunction<ServerResponse> springfoxSwaggerControllerFix(ServerProperties serverProperties,
                                                                 SpringfoxConfigurationProperties swaggerProperties) {
        val basePath = Optional.ofNullable(serverProperties.getServlet().getContextPath()).orElse("");
        val swaggerBasePath = swaggerProperties.getSwaggerUi().getBaseUrl();
        val swaggerPath = swaggerBasePath + "/swagger-ui";
        val targetUri = new URI(basePath + swaggerPath + "/index.html");

        return route(
            path(swaggerPath)
                .or(path(swaggerPath)),
            req -> ServerResponse.permanentRedirect(targetUri).build()
        );
    }

    @Bean
    Docket apiDetails() {
        return new Docket(DocumentationType.OAS_30)
            .apiInfo(new ApiInfo(
                "Spring Boot Microservice Example",
                "An MVC microservice example in Spring Boot",
                getClass().getPackage().getImplementationVersion(),
                null,
                new Contact("ascopes", "http://github.com/ascopes", null),
                "Unlicense",
                "http://unlicense.org/",
                emptyList()
            ))
            .enable(true)
            .select()
            .apis(basePackage(CONTROLLER_PACKAGE))
            .paths(any())
            .build();
    }
}
