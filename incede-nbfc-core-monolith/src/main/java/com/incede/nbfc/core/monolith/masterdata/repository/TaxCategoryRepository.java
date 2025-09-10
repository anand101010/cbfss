package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.TaxCategory;
import com.incede.nbfc.core.monolith.masterdata.dto.TaxCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxCategoryRepository extends JpaRepository<TaxCategory, Integer> {

    /**
     * Find all active TaxCategory
     *
     * @return List of active TaxCategory.
     */
    List<TaxCategoryView> findByIsDelFalseAndIsActiveTrue();
}
