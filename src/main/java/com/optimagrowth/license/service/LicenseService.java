package com.optimagrowth.license.service;

import java.util.List;
import java.util.Locale;

import com.optimagrowth.license.model.License;

public interface LicenseService {
    License getLicense(String licenseId, String organizationId, Locale locale);

    License createLicense(License license, String organizationId, Locale locale);

    License updateLicense(License license, String organizationId, Locale locale);

    void deleteLicense(String licenseId, String organizationId, Locale locale);

    List<License> getLicenses(String organizationId);
}
