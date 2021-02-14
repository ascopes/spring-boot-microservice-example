package io.ascopes.springboot.usermgmt.config;

import lombok.val;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import java.util.Optional;

import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.to;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint;

/**
 * Configures security on the endpoints that this application exposes.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String CREATE_USER_ROLE = "CREATE_USER";
    private static final String READ_USER_ROLE = "READ_USER";
    private static final String UPDATE_USER_ROLE = "UPDATE_USER";
    private static final String DELETE_USER_ROLE = "DELETE_USER";
    private static final String ADMIN_ROLE = "ADMIN_USER";

    /**
     * Configure HTTP rules.
     */
    @Override
    protected void configure(HttpSecurity security) throws Exception {
        //@formatter:off
        security
            .httpBasic().and()
            .authorizeRequests()
                // MVC controller endpoints
                .mvcMatchers(HttpMethod.GET, "/users/**").hasAnyRole(READ_USER_ROLE, ADMIN_ROLE)
                .mvcMatchers(HttpMethod.POST, "/users/**").hasAnyRole(CREATE_USER_ROLE, ADMIN_ROLE)
                .mvcMatchers(HttpMethod.PUT, "/users/**").hasAnyRole(UPDATE_USER_ROLE, ADMIN_ROLE)
                .mvcMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole(DELETE_USER_ROLE, ADMIN_ROLE)

                // Swagger
                .mvcMatchers("/docs", "/docs/**").permitAll()

                // Actuator endpoints
                .requestMatchers(to(HealthEndpoint.class)).permitAll()
                .requestMatchers(toAnyEndpoint()).hasRole(ADMIN_ROLE)

                // Anything else
                .requestMatchers(AnyRequestMatcher.INSTANCE).denyAll()
            .and()
            // Disable other stuff we don't care about.
            .csrf().disable()
            .cors().disable()
            .requestCache().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //@formatter:on
    }

    /**
     * @return A dummy user details service for providing basic auth
     * credentials to use during local testing.
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        val userDetails = User
            .withUsername("admin")
            .password("{noop}admin")
            .roles(ADMIN_ROLE)
            .build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    /**
     * @return a bean that can determine the auditor for the given request.
     */
    @Bean
    protected AuditorAware<String> auditorDetectorBean() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getName);
    }
}
