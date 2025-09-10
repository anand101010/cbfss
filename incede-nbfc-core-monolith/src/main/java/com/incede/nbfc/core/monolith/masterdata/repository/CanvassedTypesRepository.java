package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CanvassedTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.CanvassedTypesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanvassedTypesRepository extends JpaRepository<CanvassedTypes, Integer> {

    List<CanvassedTypesView> findByIsDelFalseAndIsActiveTrue();
}
