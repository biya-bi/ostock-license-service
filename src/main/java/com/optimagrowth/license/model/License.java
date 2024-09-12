package com.optimagrowth.license.model;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
@Table(name="license")
public class License extends RepresentationModel<License> {
    @Id
    @Column(nullable = false)
    private UUID id;
    
    @Column(name = "description")
    private String description;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    @Column(name = "comment")
    private String comment;

    @Transient
	private String organizationName;

	@Transient
	private String contactName;

	@Transient
	private String contactPhone;

	@Transient
	private String contactEmail;
}
