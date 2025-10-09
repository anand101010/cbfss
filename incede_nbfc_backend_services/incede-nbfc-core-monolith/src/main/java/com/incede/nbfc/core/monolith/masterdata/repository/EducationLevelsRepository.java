package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.EducationLevels;
import com.incede.nbfc.core.monolith.masterdata.dto.EducationLevelsView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EducationLevelsRepository extends JpaRepository<EducationLevels, Integer> {

    List<EducationLevelsView> findByIsDelFalseAndIsActiveTrue();

    Optional<EducationLevels> findByIdentity( UUID educationLevelId);
}
