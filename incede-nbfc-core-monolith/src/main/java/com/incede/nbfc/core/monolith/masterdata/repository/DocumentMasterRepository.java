package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentMaster;
import com.incede.nbfc.core.monolith.masterdata.dto.DocumentMasterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentMasterRepository extends JpaRepository<DocumentMaster, Integer> {

    /**
     * Find all active document master.
     *
     * @return List of active document master
     */
    List<DocumentMasterView> findByIsDelFalseAndIsActiveTrue();
}
