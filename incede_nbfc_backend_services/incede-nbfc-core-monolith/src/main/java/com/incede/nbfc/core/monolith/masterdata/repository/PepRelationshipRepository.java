package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PepRelationships;
import com.incede.nbfc.core.monolith.masterdata.dto.PepRelationshipsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PepRelationshipRepository extends JpaRepository<PepRelationships, Integer> {

    @Query(value="SELECT a FROM PepRelationships a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<PepRelationshipsView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
