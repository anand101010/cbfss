package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStatus;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadStageView;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadStatusView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadStatusRepository extends JpaRepository<LeadStatus, Integer> {

    List<LeadStatusView> findByIsDelFalseAndIsActiveTrue();
}
