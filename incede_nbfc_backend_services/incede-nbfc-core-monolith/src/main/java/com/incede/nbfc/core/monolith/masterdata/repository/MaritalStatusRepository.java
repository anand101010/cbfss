package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.MaritalStatus;
import com.incede.nbfc.core.monolith.masterdata.dto.MaritalStatusView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaritalStatusRepository extends CrudRepository<MaritalStatus, Integer> {

    /**
     * Find all active marital status
     *
     * @return List of active marital Status.
     */
    List<MaritalStatusView> findByIsDelFalseAndIsActiveTrue();

    Optional<MaritalStatus> findByIdentity( UUID maritalStatus);

    @Query(value="SELECT a FROM MaritalStatus a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<MaritalStatusView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
