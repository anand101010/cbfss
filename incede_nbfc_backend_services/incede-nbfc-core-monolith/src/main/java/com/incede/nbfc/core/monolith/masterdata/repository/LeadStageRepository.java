package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStage;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadStageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadStageRepository extends JpaRepository<LeadStage, Integer> {

    List<LeadStageView> findByIsDelFalseAndIsActiveTrue();

    Optional<LeadStage> findByIdentity(UUID leadStageIdentity);

    Optional<LeadStage>  findByIdentityAndIsDelFalseAndIsActiveTrue(UUID leadIdentity);

    @Query(value="SELECT a FROM LeadStage a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<LeadStageView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);

}
