package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Relationships;
import com.incede.nbfc.core.monolith.masterdata.dto.RelationshipsView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RelationshipsRepository extends JpaRepository<Relationships, Integer> {

        List<RelationshipsView> findByIsDelFalseAndIsActiveTrue();

    Optional<Relationships> findByIdentity( UUID relationship);
}


