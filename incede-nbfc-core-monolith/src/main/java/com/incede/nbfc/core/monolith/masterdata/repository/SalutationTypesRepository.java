package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.SalutationTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.SalutationTypesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalutationTypesRepository extends JpaRepository<SalutationTypes, Integer> {


    List<SalutationTypesView> findByIsDelFalseAndIsActiveTrue();
}
