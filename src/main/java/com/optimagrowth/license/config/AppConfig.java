package com.optimagrowth.license.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.optimagrowth.config.CrossCuttingConcernsConfig;
import com.optimagrowth.config.SecurityConfig;

@Configuration
@ComponentScan(basePackageClasses = { CrossCuttingConcernsConfig.class, SecurityConfig.class })
class AppConfig {

    @Bean
    ResourceBundleMessageSource messageSource() {
        var messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }

}
