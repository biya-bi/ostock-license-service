package com.optimagrowth.license;

import java.io.IOException;
import java.util.Map;

import org.nguiland.io.EnvFileReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@RefreshScope
@EnableFeignClients
@EnableMethodSecurity
@EntityScan("com.optimagrowth.orm.model")
public class LicensingServiceApplication {

	public static void main(String[] args) throws IOException {
		readAndSet();
		
		SpringApplication.run(LicensingServiceApplication.class, args);
	}


	private static void readAndSet() throws IOException {
		EnvFileReader.readAndSet(
				Map.of("JWT_ISSUER_URI_FILE", "spring.security.oauth2.resourceserver.jwt.issuerUri",
						"DATASOURCE_URL_FILE", "spring.datasource.url",
						"DATASOURCE_USERNAME_FILE", "spring.datasource.username",
						"DATASOURCE_PASSWORD_FILE", "spring.datasource.password"));
	}
}
