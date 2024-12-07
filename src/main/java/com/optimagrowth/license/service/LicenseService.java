package com.optimagrowth.license.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.optimagrowth.license.criteria.SearchCriteria;
import com.optimagrowth.orm.model.License;

public interface LicenseService {
    License read(UUID licenseId, UUID organizationId);

    License create(License license);

    License update(License license);

    void delete(UUID licenseId, UUID organizationId);

    List<License> read(UUID organizationId);

	Page<License> read(SearchCriteria criteria, Integer pageNumber, Integer pageSize);
}
