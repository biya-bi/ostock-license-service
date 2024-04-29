package com.optimagrowth.license.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.optimagrowth.config.CrossCuttingConcernsConfig;

@Configuration
@ComponentScan(basePackageClasses = { CrossCuttingConcernsConfig.class})
class AppConfig {
}
