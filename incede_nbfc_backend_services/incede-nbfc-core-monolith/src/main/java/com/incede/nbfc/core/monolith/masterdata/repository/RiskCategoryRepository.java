package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.RiskCategory;
import com.incede.nbfc.core.monolith.masterdata.dto.RiskCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskCategoryRepository extends JpaRepository<RiskCategory, Integer> {

    @Query(value="Select ft from RiskCategory ft where ft.isDel=false AND" +
            " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<RiskCategoryView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<RiskCategory> findByIdentity(UUID riskCategory);
}

