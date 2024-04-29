package com.optimagrowth.license.service;

import java.util.List;

import com.optimagrowth.license.model.License;

public interface LicenseService {
    License getLicense(String licenseId, String organizationId);

    License createLicense(License license, String organizationId);

    License updateLicense(License license, String organizationId);

    void deleteLicense(String licenseId, String organizationId);

    List<License> getLicenses(String organizationId);
}
