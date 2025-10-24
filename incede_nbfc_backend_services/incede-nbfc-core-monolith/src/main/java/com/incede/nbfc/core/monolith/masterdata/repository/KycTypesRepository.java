package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.KycTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.KycTypesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KycTypesRepository extends JpaRepository<KycTypes, Integer> {

    List<KycTypesView> findByIsDelFalseAndIsActiveTrue();

    Optional<KycTypes> findByIdentity(UUID idType);
}
