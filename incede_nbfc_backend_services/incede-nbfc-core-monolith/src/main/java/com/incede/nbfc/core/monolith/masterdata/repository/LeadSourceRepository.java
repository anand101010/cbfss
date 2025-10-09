package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadSource;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadSourceView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadSourceRepository extends JpaRepository<LeadSource, Integer> {

    List<LeadSourceView> findByIsDelFalseAndIsActiveTrue();

    Optional<LeadSource> findByIdentity( UUID leadSourceIdentity);
}
