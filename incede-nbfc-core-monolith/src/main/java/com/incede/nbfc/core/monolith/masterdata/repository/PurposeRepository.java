package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Purpose;
import com.incede.nbfc.core.monolith.masterdata.dto.PurposeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurposeRepository extends JpaRepository<Purpose, Integer> {

    List<PurposeView> findByIsDelFalseAndIsActiveTrue();
}
