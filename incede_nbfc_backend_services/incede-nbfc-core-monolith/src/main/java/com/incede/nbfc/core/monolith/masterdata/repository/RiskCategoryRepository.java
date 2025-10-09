package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.RiskCategory;
import com.incede.nbfc.core.monolith.masterdata.dto.RiskCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskCategoryRepository extends JpaRepository<RiskCategory, Integer> {

    List<RiskCategoryView> findByIsDelFalseAndIsActiveTrue();

    Optional<RiskCategory> findByIdentity(UUID riskCategory);
}

