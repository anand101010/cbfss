package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadSource;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadSourceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadSourceRepository extends JpaRepository<LeadSource, Integer> {

    List<LeadSourceView> findByIsDelFalseAndIsActiveTrue();

    Optional<LeadSource> findByIdentity( UUID leadSourceIdentity);

    @Query(value="SELECT a FROM LeadSource a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<LeadSourceView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
