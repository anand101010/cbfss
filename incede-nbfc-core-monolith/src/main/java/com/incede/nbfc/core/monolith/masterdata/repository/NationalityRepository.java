package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Nationality;
import com.incede.nbfc.core.monolith.masterdata.dto.NationalityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Integer> {

    /**
     * Find all active nationalities
     *
     * @return List of active nationalities.
     */
    List<NationalityView> findByIsDelFalseAndIsActiveTrue();
}
