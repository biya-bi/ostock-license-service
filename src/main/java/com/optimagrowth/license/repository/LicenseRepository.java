package com.optimagrowth.license.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.optimagrowth.license.criteria.SearchCriteria;
import com.optimagrowth.orm.model.License;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "licenseRepository")
@Repository
public interface LicenseRepository extends CrudRepository<License, UUID> {
	List<License> findByOrganizationId(UUID organizationId);

	License findByOrganizationIdAndId(UUID organizationId, UUID licenseId);

	@Query(value = "select l from License l where "
			+ "((:#{#criteria.productName} is null) or (upper(l.productName) like concat('%',upper(:#{#criteria.productName}),'%'))) and "
			+ "((:#{#criteria.licenseType} is null) or (upper(l.licenseType) like concat('%',upper(:#{#criteria.licenseType}),'%'))) and "
			+ "((:#{#criteria.description} is null) or (upper(l.description) like concat('%',upper(:#{#criteria.description}),'%'))) and "
			+ "((:#{#criteria.comment} is null) or (upper(l.comment) like concat('%',upper(:#{#criteria.comment}),'%'))) and "
			+ "((:#{#criteria.organizationName} is null) or (upper(l.organization.name) like concat('%',upper(:#{#criteria.organizationName}),'%'))) and "
			+ "((:#{#criteria.organizationId} is null) or (l.organization.id = :#{#criteria.organizationId}))", countQuery = "select count(l) from License l where "
					+ "((:#{#criteria.productName} is null) or (upper(l.productName) like concat('%',upper(:#{#criteria.productName}),'%'))) and "
					+ "((:#{#criteria.licenseType} is null) or (upper(l.licenseType) like concat('%',upper(:#{#criteria.licenseType}),'%'))) and "
					+ "((:#{#criteria.description} is null) or (upper(l.description) like concat('%',upper(:#{#criteria.description}),'%'))) and "
					+ "((:#{#criteria.comment} is null) or (upper(l.comment) like concat('%',upper(:#{#criteria.comment}),'%'))) and "
					+ "((:#{#criteria.organizationName} is null) or (upper(l.organization.name) like concat('%',upper(:#{#criteria.organizationName}),'%'))) and "
					+ "((:#{#criteria.organizationId} is null) or (l.organization.id = :#{#criteria.organizationId}))")
	Page<License> find(@Param("criteria") SearchCriteria criteria, Pageable pageable);
}
