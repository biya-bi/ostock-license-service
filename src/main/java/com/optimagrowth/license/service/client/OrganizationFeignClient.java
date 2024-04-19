package com.optimagrowth.license.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.optimagrowth.license.model.Organization;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient("gateway-service/organization-service")
@CircuitBreaker(name = "organizationFeignClient")
@Retry(name = "organizationFeignClient")
public interface OrganizationFeignClient {
    @GetMapping("/v1/organization/{organizationId}")
    Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
