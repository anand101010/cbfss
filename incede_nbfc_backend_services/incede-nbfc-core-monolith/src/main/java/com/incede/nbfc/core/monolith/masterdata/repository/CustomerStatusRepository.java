package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CustomerStatus;
import com.incede.nbfc.core.monolith.masterdata.dto.CustomerStatusView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerStatusRepository extends JpaRepository<CustomerStatus, Integer> {

    /****
     * active Customer Status.
     *
     * @return List of active Customer Status.
     */

    @Query(value ="Select ft from CustomerStatus ft where ft.isDel = false AND ft.isActive" +
            " = true and (:tenantId IS NULL or ft.tenant.tenantId = :tenantId)")
    List<CustomerStatusView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<CustomerStatus> findByIdentity( UUID customerStatus);
}
