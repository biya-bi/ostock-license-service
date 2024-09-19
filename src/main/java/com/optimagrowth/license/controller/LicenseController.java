package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;
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

import com.optimagrowth.license.dto.LicenseDto;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.translator.LicenseTranslator;
import com.optimagrowth.orm.model.License;

@RestController
@RequestMapping("/v1/license/{organizationId}")
class LicenseController {

    private final LicenseService licenseService;

    LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{licenseId}")
    ResponseEntity<LicenseDto> read(@PathVariable("organizationId") UUID organizationId,
            @PathVariable("licenseId") UUID licenseId) {
        var license = licenseService.read(licenseId, organizationId);
        return ResponseEntity.ok(toDto(organizationId, license));
    }

    @PostMapping
    ResponseEntity<LicenseDto> create(@PathVariable("organizationId") UUID organizationId,
            @RequestBody LicenseDto payload) {
        var license = LicenseTranslator.translate(payload, organizationId);
        var createdLicense = licenseService.create(license, organizationId);
        return ResponseEntity.ok(toDto(organizationId, createdLicense));
    }

    @PutMapping("/{licenseId}")
    ResponseEntity<LicenseDto> update(@PathVariable("organizationId") UUID organizationId,
            @PathVariable("licenseId") UUID licenseId,
            @RequestBody LicenseDto payload) {
        var license = LicenseTranslator.translate(payload, organizationId, licenseId);
        var updatedLicense = licenseService.update(license, organizationId);
        return ResponseEntity.ok(toDto(organizationId, updatedLicense));
    }

    @DeleteMapping("/{licenseId}")
    ResponseEntity<Void> delete(@PathVariable("organizationId") UUID organizationId,
            @PathVariable("licenseId") UUID licenseId) {
        licenseService.delete(licenseId, organizationId);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    ResponseEntity<CollectionModel<LicenseDto>> read(@PathVariable("organizationId") UUID organizationId) {
        var dtos = licenseService.read(organizationId).stream()
                .map(license -> toDto(organizationId, license)).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(dtos));
    }

    private LicenseDto toDto(UUID organizationId, License license) {
        var licenseController = methodOn(LicenseController.class);
        
        var dto = LicenseTranslator.translate(license);

        dto.add(linkTo(licenseController.read(organizationId, license.getId())).withSelfRel(),
                linkTo(licenseController.update(organizationId, license.getId(), dto)).withRel("update"),
                linkTo(licenseController.delete(organizationId, license.getId())).withRel("delete"));

        return dto;
    }

}
