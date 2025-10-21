package com.incede.nbfc.core.monolith.masterdata.repository;


import com.incede.nbfc.core.monolith.masterdata.domain.entity.Relationships;
import com.incede.nbfc.core.monolith.masterdata.dto.RelationshipsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RelationshipsRepository extends JpaRepository<Relationships, Integer> {

    List<RelationshipsView> findByIsDelFalseAndIsActiveTrue();

    Optional<Relationships> findByIdentity(UUID relationship);

    @Query(value = "SELECT a FROM Relationships a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<RelationshipsView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}


