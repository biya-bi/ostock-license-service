package com.optimagrowth.license.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.optimagrowth.dto.LicenseDto;
import com.optimagrowth.dto.PageDto;
import com.optimagrowth.license.criteria.SearchCriteria;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.service.client.OrganizationFeignClient;
import com.optimagrowth.license.translator.LicenseTranslator;
import com.optimagrowth.orm.model.License;

@RestController
@RequestMapping("/v1/license")
class LicenseController {

	private final LicenseService licenseService;

	private LicenseController licenseControllerMethodOn = methodOn(LicenseController.class);
	private OrganizationFeignClient organizationFeignClientMethodOn = methodOn(OrganizationFeignClient.class);

	LicenseController(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

	@GetMapping("/{organizationId}/{licenseId}")
	ResponseEntity<LicenseDto> read(@PathVariable("organizationId") UUID organizationId,
			@PathVariable("licenseId") UUID licenseId) {
		var license = licenseService.read(licenseId, organizationId);
		return ResponseEntity.ok(toDto(license));
	}

	@PostMapping("/{organizationId}")
	ResponseEntity<LicenseDto> create(@PathVariable("organizationId") UUID organizationId,
			@RequestBody LicenseDto payload) {
		var license = LicenseTranslator.translate(payload, organizationId);
		var createdLicense = licenseService.create(license);
		return ResponseEntity.ok(toDto(createdLicense));
	}

	@PutMapping("/{organizationId}/{licenseId}")
	ResponseEntity<LicenseDto> update(@PathVariable("organizationId") UUID organizationId,
			@PathVariable("licenseId") UUID licenseId,
			@RequestBody LicenseDto payload) {
		var license = LicenseTranslator.translate(payload, organizationId, licenseId);
		var updatedLicense = licenseService.update(license);
		return ResponseEntity.ok(toDto(updatedLicense));
	}

	@DeleteMapping("/{organizationId}/{licenseId}")
	ResponseEntity<Void> delete(@PathVariable("organizationId") UUID organizationId,
			@PathVariable("licenseId") UUID licenseId) {
		licenseService.delete(licenseId, organizationId);
		return ResponseEntity.ok(null);
	}

	@PostMapping("/search/{organizationId}")
	ResponseEntity<PageDto<LicenseDto>> read(@PathVariable("organizationId") UUID organizationId,
			@RequestBody SearchCriteria criteria,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		// Construct a new criteria object containing the organization ID in the path
		var searchCriteria = new SearchCriteria(criteria.productName(), criteria.licenseType(), criteria.description(),
				criteria.comment(), criteria.organizationName(), organizationId);

		return read(searchCriteria, pageNumber, pageSize);
	}

	@PostMapping("/search")
	ResponseEntity<PageDto<LicenseDto>> read(@RequestBody SearchCriteria criteria,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		var page = licenseService.read(criteria, pageNumber, pageSize);

		var licenses = page.getContent().stream().map(this::toDto).toList();

		var pageDto = new PageDto<>(licenses, page.getNumber(), page.getSize(), page.getTotalPages(),
				page.getNumberOfElements(), page.getTotalElements());

		return ResponseEntity.ok(pageDto);
	}

	private LicenseDto toDto(License license) {
		var organizationId = license.getOrganization().getId();
		var licenseId = license.getId();

		var dto = LicenseTranslator.translate(license);
		dto.add(linkTo(licenseControllerMethodOn.read(organizationId, licenseId)).withSelfRel(),
				linkTo(licenseControllerMethodOn.update(organizationId, licenseId, dto)).withRel("update"),
				linkTo(licenseControllerMethodOn.delete(organizationId, licenseId)).withRel("delete"),
				linkTo(organizationFeignClientMethodOn.getOrganization(organizationId)).withRel("organization"));

		return dto;
	}

}
