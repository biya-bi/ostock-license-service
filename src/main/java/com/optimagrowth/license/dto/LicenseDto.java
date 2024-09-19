package com.optimagrowth.license.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class LicenseDto extends RepresentationModel<LicenseDto> {
    private final String description;
    private final String productName;
    private final String licenseType;
    private final String comment;
}
