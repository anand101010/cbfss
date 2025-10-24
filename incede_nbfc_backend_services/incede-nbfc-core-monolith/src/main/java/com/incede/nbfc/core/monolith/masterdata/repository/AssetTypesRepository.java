package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AssetTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.AssetTypesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetTypesRepository extends JpaRepository<AssetTypes, Integer> {

    List<AssetTypesView> findByIsDelFalseAndIsActiveTrue();

    Optional<AssetTypes> findByIdentity(@NotNull(message = "Asset Type ID is required") UUID assetTypeId);
}
