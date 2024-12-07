package com.optimagrowth.license.criteria;

import java.util.UUID;

public record SearchCriteria(String productName, String licenseType, String description, String comment, String organizationName, UUID organizationId) {
}
