package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.IfscCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface IfscCodesRepository extends JpaRepository<IfscCodes, Integer> {

    Page<IfscCodes> findByIsDelFalse(Pageable pageable);
    Optional<IfscCodes> findByIfscCodeAndIsDelFalse(String ifscCode);
}
