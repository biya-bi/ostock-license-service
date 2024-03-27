package com.optimagrowth.license.service;

import java.util.Locale;

import com.optimagrowth.license.model.License;

public interface LicenseService {
    License getLicense(String licenseId, String organizationId);

    String createLicense(License license, String organizationId, Locale locale);

    String updateLicense(License license, String organizationId, Locale locale);

    String deleteLicense(String licenseId, String organizationId, Locale locale);
}
