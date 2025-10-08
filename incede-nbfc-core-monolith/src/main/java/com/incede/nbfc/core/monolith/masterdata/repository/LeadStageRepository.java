package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStage;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadStageView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadStageRepository extends JpaRepository<LeadStage, Integer> {

    List<LeadStageView> findByIsDelFalseAndIsActiveTrue();

    Optional<LeadStage> findByIdentity(UUID leadStageIdentity);
}
