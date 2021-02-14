package io.ascopes.springboot.usermgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.Collections;


/**
 * Configures what is exposed by Swagger.
 */
@Configuration
public class SpringFoxConfig {
    @Bean
    Docket apiDetails() {
        return new Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("io.ascopes.springboot.usermgmt.controller"))
            .build()
            .apiInfo(new ApiInfo(
                "Spring Boot Microservice Example",
                "An MVC microservice example in Spring Boot",
                getClass().getPackage().getImplementationVersion(),
                null,
                new Contact("ascopes", "http://github.com/ascopes", null),
                "Unlicense",
                "http://unlicense.org/",
                Collections.emptyList()
            ))
            .enable(true);
    }

    @Bean
    UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder()
            .deepLinking(true)
            .displayOperationId(false)
            .showExtensions(true)
            .validatorUrl(null)
            .build();
    }
}
