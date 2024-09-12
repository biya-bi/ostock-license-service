package com.optimagrowth.license.service;

import java.util.List;

import com.optimagrowth.license.model.License;

public interface LicenseService {
    License read(String licenseId, String organizationId);

    License create(License license, String organizationId);

    License update(License license, String organizationId);

    void delete(String licenseId, String organizationId);

    List<License> read(String organizationId);
}
