package com.optimagrowth.license.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.optimagrowth.license.exception.NotFoundException;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.service.MessageService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RateLimiter(name = "licenseService")
class LicenseServiceImpl implements LicenseService {

    private static final String LICENSE_CREATE_MESSAGE = "license.create.message";
    private static final String LICENSE_UPDATE_MESSAGE = "license.update.message";
    private static final String LICENSE_DELETE_MESSAGE = "license.delete.message";
    private static final String LICENSE_CANNOT_BE_NULL = "license.cannot.be.null";
    private static final String LICENSE_NOT_FOUND = "license.not.found";

    private final LicenseRepository licenseRepository;
    private final MessageService messageService;
    private final OrganizationFeignClient organizationFeignClient;

    LicenseServiceImpl(LicenseRepository licenseRepository, MessageService messageService,
            OrganizationFeignClient organizationFeignClient) {
        this.licenseRepository = licenseRepository;
        this.messageService = messageService;
        this.organizationFeignClient = organizationFeignClient;
    }

    @Override
    public License read(UUID licenseId, UUID organizationId) {
        var license = licenseRepository.findByOrganizationIdAndId(organizationId, licenseId);

        if (license == null) {
            throw new NotFoundException(
                    messageService.getMessage(LICENSE_NOT_FOUND, licenseId, organizationId));
        }

        var organization = organizationFeignClient.getOrganization(organizationId);

        if (organization != null) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }

        return license;
    }

    @Override
    public License create(License license, UUID organizationId) {
        Objects.requireNonNull(license, messageService.getMessage(LICENSE_CANNOT_BE_NULL, license));

        license.setId(UUID.randomUUID());
        license.setOrganizationId(organizationId);

        var newLicense = licenseRepository.save(license);

        log.info(messageService.getMessage(LICENSE_CREATE_MESSAGE, license));

        return newLicense;
    }

    @Override
    public License update(License license, UUID organizationId) {
        Objects.requireNonNull(license, messageService.getMessage(LICENSE_CANNOT_BE_NULL, license));

        license.setOrganizationId(organizationId);

        var updatedLicense = licenseRepository.save(license);

        log.info(messageService.getMessage(LICENSE_UPDATE_MESSAGE, license));

        return updatedLicense;
    }

    @Override
    public void delete(UUID licenseId, UUID organizationId) {
        var license = licenseRepository.findByOrganizationIdAndId(organizationId, licenseId);

        if (license == null) {
            throw new NotFoundException(
                    messageService.getMessage(LICENSE_NOT_FOUND, licenseId, organizationId));
        }

        licenseRepository.delete(license);

        log.info(messageService.getMessage(LICENSE_DELETE_MESSAGE, licenseId, organizationId));
    }

    @Override
    public List<License> read(UUID organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }

}
