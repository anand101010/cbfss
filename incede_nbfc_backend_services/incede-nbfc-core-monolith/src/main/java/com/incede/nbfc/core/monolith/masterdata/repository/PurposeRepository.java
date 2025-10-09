package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Purpose;
import com.incede.nbfc.core.monolith.masterdata.dto.PurposeView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Integer> {

    List<PurposeView> findByIsDelFalseAndIsActiveTrue();

    Optional<Purpose> findByIdentity(@NotNull(message = "Purpose is required") UUID purposeId);
}
