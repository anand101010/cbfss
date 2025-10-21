package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Banks;
import com.incede.nbfc.core.monolith.masterdata.dto.BanksView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BanksRepository extends JpaRepository<Banks, Integer> {

    @Query(value="Select ft from Banks ft where ft.isDel=false AND" +
            " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<BanksView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);
}
