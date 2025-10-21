package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.dto.FollowUpTypeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowUpTypeRepository extends JpaRepository<FollowUpType, Integer> {

    List<FollowUpTypeView> findByIsDelFalseAndIsActiveTrue();

    Optional<FollowUpType> findByIdentity(UUID followUpTypeId);

    @Query(value="SELECT a FROM FollowUpType a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<FollowUpTypeView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
