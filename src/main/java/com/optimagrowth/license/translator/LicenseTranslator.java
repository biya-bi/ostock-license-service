package com.optimagrowth.license.translator;

import com.optimagrowth.license.dto.LicenseDto;
import com.optimagrowth.orm.model.License;
import com.optimagrowth.orm.model.Organization;

public final class LicenseTranslator {
    private LicenseTranslator() {
    }

    public static LicenseDto translate(License license) {
        return new LicenseDto(license.getId(), license.getDescription(), license.getOrganization().getId(),
                license.getProductName(), license.getLicenseType(), license.getComment());
    }

    public static License translate(LicenseDto dto) {
        var organization = new Organization();
        organization.setId(dto.getOrganizationId());

        var license = new License();

        license.setId(dto.getId());
        license.setDescription(dto.getDescription());
        license.setOrganization(organization);
        license.setProductName(dto.getProductName());
        license.setLicenseType(dto.getLicenseType());
        license.setComment(dto.getComment());

        return license;
    }
}
