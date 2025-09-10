package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Genders;
import com.incede.nbfc.core.monolith.masterdata.dto.GendersView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GendersRepository extends JpaRepository<Genders, Integer> {

    /**
     * Find all active genders
     *
     * @return List of active Customer Status.
     */
    List<GendersView> findByIsDelFalseAndIsActiveTrue();
}
