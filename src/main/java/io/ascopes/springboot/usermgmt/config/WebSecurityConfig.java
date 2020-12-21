package io.ascopes.springboot.usermgmt.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String CREATE_USER_ROLE = "CREATE_USER";
    private static final String READ_USER_ROLE = "READ_USER";
    private static final String UPDATE_USER_ROLE = "UPDATE_USER";
    private static final String DELETE_USER_ROLE = "DELETE_USER";
    private static final String ADMIN_ROLE = "ADMIN_USER";

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        //@formatter:off
        security
            .csrf().disable()
            .cors().disable()
            .sessionManagement()
                .enableSessionUrlRewriting(false)
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .disable()
            .httpBasic().and()
            .authorizeRequests(http -> http
                .mvcMatchers("/actuator/health").permitAll()
                .mvcMatchers("/actuator/**").hasRole(ADMIN_ROLE)
                .mvcMatchers(HttpMethod.GET, "/users/**").hasAnyRole(READ_USER_ROLE, ADMIN_ROLE)
                .mvcMatchers(HttpMethod.POST, "/users/**").hasAnyRole(CREATE_USER_ROLE, ADMIN_ROLE)
                .mvcMatchers(HttpMethod.PUT, "/users/**").hasAnyRole(UPDATE_USER_ROLE, ADMIN_ROLE)
                .mvcMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole(DELETE_USER_ROLE, ADMIN_ROLE)
            );
        //@formatter:on
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        // Obviously this is really insecure. You would integrate this with OAuth2 or a backend database instead. This
        // is just for a simple example that authorization does work.
        val userDetails = User
            .withUsername("admin")
            .password("{noop}admin")
            .roles(ADMIN_ROLE)
            .build();

        return new InMemoryUserDetailsManager(userDetails);
    }
}
