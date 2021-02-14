package io.ascopes.springboot.usermgmt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;


/**
 * Application entrypoint.
 */
@EnableJpaAuditing
@EnableJpaRepositories
@EnableOpenApi
@EnableTransactionManagement
@EnableWebMvc
@EnableWebSecurity
@Slf4j
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        try {
            SpringApplication.run(App.class, args);
        } catch (Throwable ex) {
            log.error("Fatal exception thrown by Spring application context:", ex);
            System.exit(ex instanceof BeanInstantiationException ? 1 : 2);
        }
    }
}
