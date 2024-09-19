package com.optimagrowth.license.translator;

import java.util.UUID;

import com.optimagrowth.dto.LicenseDto;
import com.optimagrowth.orm.model.License;
import com.optimagrowth.orm.model.Organization;

public final class LicenseTranslator {
    private LicenseTranslator() {
    }

    public static LicenseDto translate(License license) {
        return new LicenseDto(license.getDescription(), license.getProductName(), license.getLicenseType(),
                license.getComment());
    }

    public static License translate(LicenseDto dto, UUID organizationId) {
        return translate(dto, organizationId, null);
    }

    public static License translate(LicenseDto dto, UUID organizationId, UUID licenseId) {
        var organization = new Organization();
        organization.setId(organizationId);

        var license = new License();

        license.setId(licenseId);
        license.setDescription(dto.getDescription());
        license.setOrganization(organization);
        license.setProductName(dto.getProductName());
        license.setLicenseType(dto.getLicenseType());
        license.setComment(dto.getComment());

        return license;
    }
}
