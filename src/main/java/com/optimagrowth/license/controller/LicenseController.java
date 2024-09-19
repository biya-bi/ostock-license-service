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
        var dto = LicenseTranslator.translate(license);
        return ResponseEntity.ok(addLinks(organizationId, dto));
    }

    @PostMapping
    ResponseEntity<LicenseDto> create(@PathVariable("organizationId") UUID organizationId,
            @RequestBody LicenseDto payload) {
        var license = LicenseTranslator.translate(payload);
        var createdLicense = licenseService.create(license, organizationId);
        var dto = LicenseTranslator.translate(createdLicense);
        return ResponseEntity.ok(addLinks(organizationId, dto));
    }

    @PutMapping
    ResponseEntity<LicenseDto> update(@PathVariable("organizationId") UUID organizationId,
            @RequestBody LicenseDto payload) {
        var license = LicenseTranslator.translate(payload);
        var updatedLicense = licenseService.update(license, organizationId);
        var dto = LicenseTranslator.translate(updatedLicense);
        return ResponseEntity.ok(addLinks(organizationId, dto));
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
                .map(license -> this.addLinks(organizationId, LicenseTranslator.translate(license))).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(dtos));
    }

    private LicenseDto addLinks(UUID organizationId, LicenseDto dto) {
        var licenseController = methodOn(LicenseController.class);
        return dto.add(linkTo(licenseController.read(organizationId, dto.getId())).withSelfRel(),
                linkTo(licenseController.update(organizationId, dto)).withRel("update"),
                linkTo(licenseController.delete(organizationId, dto.getId())).withRel("delete"));
    }

}
