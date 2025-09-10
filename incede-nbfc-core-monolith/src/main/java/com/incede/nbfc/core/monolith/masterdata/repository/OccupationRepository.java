package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Occupation;
import com.incede.nbfc.core.monolith.masterdata.dto.OccupationView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OccupationRepository extends CrudRepository<Occupation, Integer> {

    /**
     * Find all active occupations
     *
     * @return List of active occupations.
     */
    List<OccupationView> findByIsDelFalseAndIsActiveTrue();
}
