package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

@RestController
@RequestMapping("/v1/license/{organizationId}")
class LicenseController {

    private final LicenseService licenseService;

    LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{licenseId}")
    ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicense(licenseId, organizationId);
        return ResponseEntity.ok(addLinks(organizationId, license));
    }

    @PostMapping
    ResponseEntity<License> createLicense(@PathVariable("organizationId") String organizationId,
            @RequestBody License license) {
        licenseService.createLicense(license, organizationId);
        return ResponseEntity.ok(addLinks(organizationId, license));
    }

    @PutMapping
    ResponseEntity<License> updateLicense(@PathVariable("organizationId") String organizationId,
            @RequestBody License license) {
        licenseService.updateLicense(license, organizationId);
        return ResponseEntity.ok(addLinks(organizationId, license));
    }

    @DeleteMapping("/{licenseId}")
    ResponseEntity<Void> deleteLicense(@PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        licenseService.deleteLicense(licenseId, organizationId);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    ResponseEntity<CollectionModel<License>> getLicenses(@PathVariable("organizationId") String organizationId) {
        var licenses = licenseService.getLicenses(organizationId).stream()
                .map(license -> this.addLinks(organizationId, license)).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(licenses));
    }

    private License addLinks(String organizationId, License license) {
        LicenseController methodOn = methodOn(LicenseController.class);
        return license.add(linkTo(methodOn.getLicense(organizationId, license.getId())).withSelfRel(),
                linkTo(methodOn.updateLicense(organizationId, license)).withRel("update"),
                linkTo(methodOn.deleteLicense(organizationId, license.getId())).withRel("delete"));
    }

}
