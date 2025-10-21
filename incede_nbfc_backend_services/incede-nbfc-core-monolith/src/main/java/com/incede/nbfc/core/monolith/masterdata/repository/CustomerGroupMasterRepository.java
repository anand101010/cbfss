package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CustomerGroupMaster;
import com.incede.nbfc.core.monolith.masterdata.dto.CustomerGroupMasterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerGroupMasterRepository extends JpaRepository<CustomerGroupMaster, Integer> {

    @Query(value="Select ft from CustomerGroupMaster ft where ft.isDel=false AND" +
            " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<CustomerGroupMasterView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<CustomerGroupMaster> findByIdentity(UUID customerGroupId);
}
