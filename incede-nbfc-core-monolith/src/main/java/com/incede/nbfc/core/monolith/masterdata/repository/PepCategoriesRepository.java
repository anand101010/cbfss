package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PepCategories;
import com.incede.nbfc.core.monolith.masterdata.dto.PepCategoriesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PepCategoriesRepository extends JpaRepository<PepCategories, Integer> {

    List<PepCategoriesView> findByIsActiveTrue();
}
