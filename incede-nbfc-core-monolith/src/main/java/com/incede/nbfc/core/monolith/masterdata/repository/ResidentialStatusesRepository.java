package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ResidentialStatuses;
import com.incede.nbfc.core.monolith.masterdata.dto.ResidentialStatusesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResidentialStatusesRepository extends JpaRepository<ResidentialStatuses, Integer> {

    List<ResidentialStatusesView> findByIsActiveTrue();

    Optional<ResidentialStatuses> findByIdentity( UUID residentialStatusId);
}
