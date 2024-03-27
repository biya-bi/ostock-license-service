package com.optimagrowth.license.service.impl;

import java.util.Locale;
import java.util.Random;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

@Service
class LicenseServiceImpl implements LicenseService {

    private final Random random = new Random();
    private final MessageSource messageSource;

    LicenseServiceImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public License getLicense(String licenseId, String organizationId) {
        License license = new License();
        license.setId(random.nextInt(1000));
        license.setLicenseId(licenseId);
        license.setOrganizationId(organizationId);
        license.setDescription("Software product");
        license.setProductName("Ostock");
        license.setLicenseType("full");
        return license;
    }

    @Override
    public String createLicense(License license, String organizationId, Locale locale) {
        if (license != null) {
            license.setOrganizationId(organizationId);
            return messageSource.getMessage("license.create.message", new Object[] { license }, locale);
        }
        return null;
    }

    @Override
    public String updateLicense(License license, String organizationId, Locale locale) {
        if (license != null) {
            license.setOrganizationId(organizationId);
            return messageSource.getMessage("license.update.message", new Object[] { license }, locale);
        }
        return null;
    }

    @Override
    public String deleteLicense(String licenseId, String organizationId, Locale locale) {
        return messageSource.getMessage("license.delete.message", new Object[] { licenseId, organizationId }, locale);
    }
}
