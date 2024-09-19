package com.optimagrowth.license.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.optimagrowth.license.exception.NotFoundException;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.orm.model.License;
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

    LicenseServiceImpl(LicenseRepository licenseRepository, MessageService messageService) {
        this.licenseRepository = licenseRepository;
        this.messageService = messageService;
    }

    @Override
    public License read(UUID licenseId, UUID organizationId) {
        var license = licenseRepository.findByOrganizationIdAndId(organizationId, licenseId);

        if (license == null) {
            throw new NotFoundException(
                    messageService.getMessage(LICENSE_NOT_FOUND, licenseId, organizationId));
        }

        return license;
    }

    @Override
    public License create(License license) {
        Objects.requireNonNull(license, messageService.getMessage(LICENSE_CANNOT_BE_NULL, license));

        license.setId(UUID.randomUUID());

        var createdLicense = licenseRepository.save(license);

        log.info(messageService.getMessage(LICENSE_CREATE_MESSAGE, license));

        return createdLicense;
    }

    @Override
    public License update(License license) {
        Objects.requireNonNull(license, messageService.getMessage(LICENSE_CANNOT_BE_NULL, license));

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
