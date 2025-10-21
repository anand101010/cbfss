package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.KycTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.KycTypesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KycTypesRepository extends JpaRepository<KycTypes, Integer> {

    @Query(value="Select ft from KycTypes ft where ft.isDel=false AND" +
            " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<KycTypesView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<KycTypes> findByIdentity(UUID idType);
}
