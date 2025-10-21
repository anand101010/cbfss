package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ReferralSources;
import com.incede.nbfc.core.monolith.masterdata.dto.ReferralSourcesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReferralSourceRepository extends JpaRepository<ReferralSources, Integer> {

    List<ReferralSourcesView> findByIsDelFalseAndIsActiveTrue();

    Optional<ReferralSources> findByIdentity( UUID referralSourceId);

    @Query(value="SELECT a FROM ReferralSources a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<ReferralSourcesView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);

}
