package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ResidentialStatuses;
import com.incede.nbfc.core.monolith.masterdata.dto.ResidentialStatusesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResidentialStatusesRepository extends JpaRepository<ResidentialStatuses, Integer> {

    @Query(value="Select ft from ResidentialStatuses ft where ft.isDel=false AND" +
            " (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<ResidentialStatusesView> findByIsActiveTrueByTenantId(Integer tenantId);

    Optional<ResidentialStatuses> findByIdentity( UUID residentialStatusId);
}
