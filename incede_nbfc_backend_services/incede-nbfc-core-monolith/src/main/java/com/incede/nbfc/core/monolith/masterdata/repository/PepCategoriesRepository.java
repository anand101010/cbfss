package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PepCategories;
import com.incede.nbfc.core.monolith.masterdata.dto.PepCategoriesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PepCategoriesRepository extends JpaRepository<PepCategories, Integer> {


    @Query(value="SELECT a FROM PepCategories a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<PepCategoriesView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
