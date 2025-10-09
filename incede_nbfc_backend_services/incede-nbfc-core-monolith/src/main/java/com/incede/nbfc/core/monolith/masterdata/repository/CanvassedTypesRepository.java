package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CanvassedTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.CanvassedTypesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CanvassedTypesRepository extends JpaRepository<CanvassedTypes, Integer> {

    List<CanvassedTypesView> findByIsDelFalseAndIsActiveTrue();

    Optional<CanvassedTypes> findByIdentity(@NotNull(message = "Canvassed type is required") UUID canvassedTypeId);
}
