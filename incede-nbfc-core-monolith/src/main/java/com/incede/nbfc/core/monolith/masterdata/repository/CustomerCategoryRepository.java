package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CustomerCategory;
import com.incede.nbfc.core.monolith.masterdata.dto.CustomerCategoryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCategoryRepository extends JpaRepository<CustomerCategory, Integer> {

    List<CustomerCategoryView> findByIsDelFalseAndIsActiveTrue();
}
