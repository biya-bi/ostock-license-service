package com.optimagrowth.license.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;

import com.optimagrowth.license.criteria.SearchCriteria;
import com.optimagrowth.orm.model.License;
import com.optimagrowth.orm.model.Organization;

import jakarta.persistence.EntityManager;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LicenseRepository.class))
class LicenseRepositoryTest {

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	private EntityManager entityManager;

	private final Organization unitedNations = constructOrganization(UUID.randomUUID(), "United Nations",
			"USG Mr. Li Junhua", "population@un.org",
			"+1-212-963-3209");

	private final Organization nasa = constructOrganization(UUID.randomUUID(), "NASA",
			"Kelly Garcia", "kelly.l.garcia@nasa.gov",
			"+1-650-604-3273");

	private final License msOffice2016 = constructLicense(UUID.randomUUID(), "Microsoft Office 2016", "Software",
			"This license is for the Kennedy Space Station", "Microsoft Office 2016 license for NASA", nasa);

	private final License powerEdgeRackServer = constructLicense(UUID.randomUUID(), "PowerEdge XE9680 Rack Server",
			"Hardware",
			"Should be delivered before month end",
			"Take on demanding artificial intelligence, machine learning and deep learning", unitedNations);

	private final List<Organization> organizations = Arrays.asList(unitedNations, nasa);

	private final List<License> licenses = Arrays.asList(msOffice2016, powerEdgeRackServer);

	private final String nonExistent = "nonExistent";

	@BeforeEach
	void setUp() {
		organizations.forEach(entityManager::persist);
		licenses.forEach(entityManager::persist);
	}

	@AfterEach
	void cleanUp() {
		licenses.forEach(license -> licenseRepository.deleteById(license.getId()));
	}

	@Test
	void find_ExactProductNameIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(msOffice2016.getProductName(), null, null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_LowercaseProductNameIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(msOffice2016.getProductName().toLowerCase(), null, null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_FirstThreeProductNameCharactersAreGiven_ReturnLicense() {
		var criteria = new SearchCriteria(msOffice2016.getProductName().substring(0, 3), null, null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_NoCriterionIsGiven_ReturnLicenses() {
		var criteria = new SearchCriteria(null, null, null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		assertEquals(licenses.size(), page.getTotalElements());
	}

	@Test
	void find_NonExistentNameIsGiven_NoReturnLicense() {
		var criteria = new SearchCriteria(nonExistent, null, null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		assertTrue(page.isEmpty());
	}

	@Test
	void find_ExactLicenseTypeIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, msOffice2016.getLicenseType(), null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_LowercaseLicenseTypeIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, msOffice2016.getLicenseType().toLowerCase(), null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_FirstThreeLicenseTypeCharactersAreGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, msOffice2016.getLicenseType().substring(0, 3), null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_NonExistentLicenseTypeIsGiven_NoReturnLicense() {
		var criteria = new SearchCriteria(null, nonExistent, null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		assertTrue(page.isEmpty());
	}

	@Test
	void find_ExactDescriptionIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, msOffice2016.getDescription(), null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_LowercaseDescriptionIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, msOffice2016.getDescription().toLowerCase(), null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_FirstThreeContactEmailCharactersAreGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, msOffice2016.getDescription().substring(0, 3), null, null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_NonExistentDescriptionIsGiven_NoReturnLicense() {
		var criteria = new SearchCriteria(null, null, nonExistent, null, null, null);

		var page = licenseRepository.find(criteria, null);

		assertTrue(page.isEmpty());
	}

	@Test
	void find_ExactCommentIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, msOffice2016.getComment(), null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_LowercaseCommentIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, msOffice2016.getComment().toLowerCase(), null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_FirstThreeCommentCharactersAreGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, msOffice2016.getComment().substring(0, 3), null, null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), msOffice2016.getId()));
	}

	@Test
	void find_NonExistentCommentIsGiven_NoReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, nonExistent, null, null);

		var page = licenseRepository.find(criteria, null);

		assertTrue(page.isEmpty());
	}

	@Test
	void find_ProductNameExistsButLicenseTypeDoesNot_NoReturnLicense() {
		var criteria = new SearchCriteria(msOffice2016.getProductName(), nonExistent, null, null, null, null);

		var page = licenseRepository.find(criteria, null);

		assertTrue(page.isEmpty());
	}

	@Test
	void find_PageNumberIs0AndPageSizeIs1_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, null, null, null);

		var page = licenseRepository.find(criteria, PageRequest.of(0, 1));

		var license1 = page.getContent().stream().filter(license -> license.getId().equals(msOffice2016.getId()))
				.findFirst().orElse(null);

		assertNotNull(license1);
		assertEquals(0, page.getNumber());
		assertEquals(licenses.size(), page.getTotalPages());
	}

	@Test
	void find_PageNumberIs1AndPageSizeIs1_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, null, null, null);

		var page = licenseRepository.find(criteria, PageRequest.of(1, 1));

		var license1 = page.getContent().stream()
				.filter(license -> license.getId().equals(powerEdgeRackServer.getId())).findFirst().orElse(null);

		assertNotNull(license1);
		assertEquals(1, page.getNumber());
		assertEquals(licenses.size(), page.getTotalPages());
	}

	@Test
	void find_OrganizationNameIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, null, unitedNations.getName(), null);

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), powerEdgeRackServer.getId()));
	}

	@Test
	void find_OrganizationIdIsGiven_ReturnLicense() {
		var criteria = new SearchCriteria(null, null, null, null, null, unitedNations.getId());

		var page = licenseRepository.find(criteria, null);

		// Verify that the total number of licenses returned is 1
		assertEquals(1, page.getTotalElements());
		// Verify that the returned license is right one
		assertTrue(exists(page.getContent(), powerEdgeRackServer.getId()));
	}

	@Test
	void find_NonExistentOrganizationIdIsGiven_ReturnNoLicense() {
		var criteria = new SearchCriteria(null, null, null, null, null, UUID.randomUUID());

		var page = licenseRepository.find(criteria, null);

		// Verify that the returned license is right one
		assertTrue(page.isEmpty());
	}

	private License constructLicense(UUID id, String productName, String licenseType,
			String comment, String description, Organization organization) {
		var license = new License();
		license.setId(id);
		license.setProductName(productName);
		license.setLicenseType(licenseType);
		license.setComment(comment);
		license.setDescription(description);
		license.setOrganization(organization);
		return license;
	}

	private Organization constructOrganization(UUID id, String organizationName, String contactName,
			String contactEmail, String contactPhone) {
		var organization = new Organization();
		organization.setId(id);
		organization.setName(organizationName);
		organization.setContactName(contactName);
		organization.setContactEmail(contactEmail);
		organization.setContactPhone(contactPhone);
		return organization;
	}

	private boolean exists(List<License> licenses, UUID id) {
		return licenses.stream().anyMatch(license -> license.getId().equals(id));
	}

}