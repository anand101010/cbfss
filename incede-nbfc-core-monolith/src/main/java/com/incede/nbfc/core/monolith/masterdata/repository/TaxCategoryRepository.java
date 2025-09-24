package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.TaxCategory;
import com.incede.nbfc.core.monolith.masterdata.dto.TaxCategoryView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxCategoryRepository extends JpaRepository<TaxCategory, Integer> {

    /**
     * Find all active TaxCategory
     *
     * @return List of active TaxCategory.
     */
    List<TaxCategoryView> findByIsDelFalseAndIsActiveTrue();

    Optional<TaxCategory> findByIdentity( UUID taxCategory);
}
