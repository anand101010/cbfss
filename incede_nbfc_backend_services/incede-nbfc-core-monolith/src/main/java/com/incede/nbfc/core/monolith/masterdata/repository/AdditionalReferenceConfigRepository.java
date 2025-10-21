package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import com.incede.nbfc.core.monolith.masterdata.dto.AdditionalReferenceConfigView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdditionalReferenceConfigRepository extends JpaRepository<AdditionalReferenceConfig, Integer> {

    List<AdditionalReferenceConfigView> findByIsDelFalseAndIsActiveTrue();
    Optional<AdditionalReferenceConfig> findByIdentity(UUID identity);

    @Query(value="SELECT a FROM AdditionalReferenceConfig a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<AdditionalReferenceConfigView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);

    @Query("SELECT a FROM AdditionalReferenceConfig a WHERE a.isDel = false AND a.isActive = true AND a.tenant.tenantId = :tenantId AND a.productServiceId = :productServiceId")
    List<AdditionalReferenceConfig> findRefernceActiveByTenantAndProduct(
            @Param("tenantId") Integer tenantId,
            @Param("productServiceId") Integer productServiceId
    );
}
