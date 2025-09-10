package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ResidentialStatuses;
import com.incede.nbfc.core.monolith.masterdata.dto.ResidentialStatusesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidentialStatusesRepository extends JpaRepository<ResidentialStatuses, Integer> {

    List<ResidentialStatusesView> findByIsActiveTrue();
}
