package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CustomerCategory;
import com.incede.nbfc.core.monolith.masterdata.dto.CustomerCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerCategoryRepository extends JpaRepository<CustomerCategory, Integer> {

    @Query(value="Select ft from CustomerCategory ft where ft.isDel=false AND" +
            " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<CustomerCategoryView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<CustomerCategory> findByIdentity(UUID categoryId);
}
