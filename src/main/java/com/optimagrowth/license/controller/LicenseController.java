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
    ResponseEntity<License> read(@PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        var license = licenseService.read(licenseId, organizationId);
        return ResponseEntity.ok(addLinks(organizationId, license));
    }

    @PostMapping
    ResponseEntity<License> create(@PathVariable("organizationId") String organizationId,
            @RequestBody License license) {
        var newLicense = licenseService.create(license, organizationId);
        return ResponseEntity.ok(addLinks(organizationId, newLicense));
    }

    @PutMapping
    ResponseEntity<License> update(@PathVariable("organizationId") String organizationId,
            @RequestBody License license) {
        var updatedLicense = licenseService.update(license, organizationId);
        return ResponseEntity.ok(addLinks(organizationId, updatedLicense));
    }

    @DeleteMapping("/{licenseId}")
    ResponseEntity<Void> delete(@PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId) {
        licenseService.delete(licenseId, organizationId);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    ResponseEntity<CollectionModel<License>> read(@PathVariable("organizationId") String organizationId) {
        var licenses = licenseService.read(organizationId).stream()
                .map(license -> this.addLinks(organizationId, license)).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(licenses));
    }

    private License addLinks(String organizationId, License license) {
        var licenseController = methodOn(LicenseController.class);
        return license.add(linkTo(licenseController.read(organizationId, license.getId())).withSelfRel(),
                linkTo(licenseController.update(organizationId, license)).withRel("update"),
                linkTo(licenseController.delete(organizationId, license.getId())).withRel("delete"));
    }

}
