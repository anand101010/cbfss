package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Designations;
import com.incede.nbfc.core.monolith.masterdata.dto.DesignationsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignationsRepository extends JpaRepository<Designations, Integer> {

    List<DesignationsView> findByIsActiveTrue();
}
