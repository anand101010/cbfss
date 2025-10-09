package com.incede.nbfc.core.monolith.tenant.repository;

import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> { // Keep as Integer to match entity

    Optional<Tenant> findByIdentity(UUID identity);

    Optional<Tenant> findByTenantCode(String tenantCode);
    boolean existsByTenantCode(String tenantCode);
    Optional<Tenant> findByTenantCodeAndIsActiveTrue(String tenantCode);
}