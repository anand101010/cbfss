package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.SalutationTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.SalutationTypesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalutationTypesRepository extends JpaRepository<SalutationTypes, Integer> {


    List<SalutationTypesView> findByIsDelFalseAndIsActiveTrue();

    Optional<SalutationTypes> findByIdentity(UUID salutation);
}
