package com.optimagrowth.license.dto;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class LicenseDto extends RepresentationModel<LicenseDto> {
    private final UUID id;
    private final String description;
    private final UUID organizationId;
    private final String productName;
    private final String licenseType;
    private final String comment;
}
