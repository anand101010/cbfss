package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.SourceOfIncomeType;
import com.incede.nbfc.core.monolith.masterdata.dto.SourceOfIncomeTypeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceOfIncomeTypeRepository extends JpaRepository<SourceOfIncomeType, Integer> {

    List<SourceOfIncomeTypeView> findByIsDelFalseAndIsActiveTrue();
}
