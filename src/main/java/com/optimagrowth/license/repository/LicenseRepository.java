package com.optimagrowth.license.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.optimagrowth.license.model.License;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "licenseRepository")
@Repository
public interface LicenseRepository extends CrudRepository<License, UUID> {
    List<License> findByOrganizationId(UUID organizationId);

    License findByOrganizationIdAndId(UUID organizationId, UUID licenseId);
}
