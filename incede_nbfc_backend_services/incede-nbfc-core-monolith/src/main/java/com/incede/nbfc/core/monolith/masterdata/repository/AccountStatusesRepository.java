package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountStatuses;
import com.incede.nbfc.core.monolith.masterdata.dto.AccountStatusesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountStatusesRepository extends JpaRepository<AccountStatuses, Integer> {


    @Query(value="Select ft from AccountStatuses ft where ft.isDel=false AND" +
            " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<AccountStatusesView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<AccountStatuses> findByIdentity( UUID accountStatus);
}
