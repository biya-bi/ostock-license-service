package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Locale;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
class LicenseController {

    private final LicenseService licenseService;

    LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{licenseId}")
    ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        License license = licenseService.getLicense(licenseId, organizationId, locale);
        return ResponseEntity.ok(addLinks(organizationId, license, locale));
    }

    @PostMapping
    ResponseEntity<License> createLicense(@PathVariable("organizationId") String organizationId,
            @RequestBody License license, @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        licenseService.createLicense(license, organizationId, locale);
        return ResponseEntity.ok(addLinks(organizationId, license, locale));
    }

    @PutMapping
    ResponseEntity<License> updateLicense(@PathVariable("organizationId") String organizationId,
            @RequestBody License license, @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        licenseService.updateLicense(license, organizationId, locale);
        return ResponseEntity.ok(addLinks(organizationId, license, locale));
    }

    @DeleteMapping("/{licenseId}")
    ResponseEntity<Void> deleteLicense(@PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        licenseService.deleteLicense(licenseId, organizationId, locale);
        return ResponseEntity.ok(null);
    }

    private License addLinks(String organizationId, License license, Locale locale) {
        LicenseController methodOn = methodOn(LicenseController.class);
        return license.add(linkTo(methodOn.getLicense(organizationId, license.getLicenseId(), locale)).withSelfRel(),
                linkTo(methodOn.updateLicense(organizationId, license, locale)).withRel("update"),
                linkTo(methodOn.deleteLicense(organizationId, license.getLicenseId(), locale)).withRel("delete"));
    }

}
