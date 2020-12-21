package io.ascopes.springboot.usermgmt.config;

import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ValidationConfig implements WebMvcConfigurer {
    @Bean(autowireCandidate = false)
    ReloadableResourceBundleMessageSource validationMessageSource() {
        val source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Override
    @Bean
    public Validator getValidator() {
        val validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(validationMessageSource());
        return validator;
    }
}
