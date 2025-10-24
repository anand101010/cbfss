package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PepVerificationSources;
import com.incede.nbfc.core.monolith.masterdata.dto.PepVerificationSourceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PepVerificationSourceRepository extends JpaRepository<PepVerificationSources, Integer> {

    List<PepVerificationSourceView> findByIsActiveTrue();
}
