package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import com.incede.nbfc.core.monolith.masterdata.dto.AdditionalReferenceConfigView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdditionalReferenceConfigRepository extends JpaRepository<AdditionalReferenceConfig, Integer> {

    List<AdditionalReferenceConfigView> findByIsDelFalseAndIsActiveTrue();
}
