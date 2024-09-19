package com.optimagrowth.license.service;

import java.util.List;
import java.util.UUID;

import com.optimagrowth.orm.model.License;

public interface LicenseService {
    License read(UUID licenseId, UUID organizationId);

    License create(License license, UUID organizationId);

    License update(License license, UUID organizationId);

    void delete(UUID licenseId, UUID organizationId);

    List<License> read(UUID organizationId);
}
