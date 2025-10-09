package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Designations;
import com.incede.nbfc.core.monolith.masterdata.dto.DesignationsView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignationsRepository extends JpaRepository<Designations, Integer> {

    List<DesignationsView> findByIsActiveTrue();

    Optional<Designations> findByIdentity(@NotNull(message = "Designation ID is required") UUID designationId);
}
