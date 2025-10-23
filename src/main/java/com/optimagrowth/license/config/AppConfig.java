package com.optimagrowth.license.config;

import org.nguiland.context.config.MessageConfig;
import org.nguiland.web.security.config.SecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.optimagrowth.config.CrossCuttingConcernsConfig;

@Configuration
@ComponentScan(basePackageClasses = { CrossCuttingConcernsConfig.class, MessageConfig.class, SecurityConfig.class })
class AppConfig {
}
