package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStatus;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadStatusView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadStatusRepository extends JpaRepository<LeadStatus, Integer> {

    List<LeadStatusView> findByIsDelFalseAndIsActiveTrue();

    Optional<LeadStatus> findByIdentity( UUID leadStatusIdentity);

    @Query(value="SELECT a FROM LeadStatus a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<LeadStatusView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
