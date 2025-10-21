package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PepVerificationSources;
import com.incede.nbfc.core.monolith.masterdata.dto.PepVerificationSourceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PepVerificationSourceRepository extends JpaRepository<PepVerificationSources, Integer> {

    @Query(value="SELECT a FROM PepVerificationSources a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<PepVerificationSourceView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
