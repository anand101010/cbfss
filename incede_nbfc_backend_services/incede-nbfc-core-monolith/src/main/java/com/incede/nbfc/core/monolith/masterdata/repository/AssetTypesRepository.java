package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AssetTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.AssetTypesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetTypesRepository extends JpaRepository<AssetTypes, Integer> {

    @Query(value="Select ft from AssetTypes ft where ft.isDel=false AND" +
            " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<AssetTypesView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<AssetTypes> findByIdentity(@NotNull(message = "Asset Type ID is required") UUID assetTypeId);
}
