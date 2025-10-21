package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Purpose;
import com.incede.nbfc.core.monolith.masterdata.dto.PurposeView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Integer> {

    List<PurposeView> findByIsDelFalseAndIsActiveTrue();

    Optional<Purpose> findByIdentity(@NotNull(message = "Purpose is required") UUID purposeId);

    @Query(value="SELECT a FROM Purpose a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<PurposeView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
