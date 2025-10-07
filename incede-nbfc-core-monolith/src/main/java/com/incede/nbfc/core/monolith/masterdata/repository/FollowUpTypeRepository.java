package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.dto.FollowUpTypeView;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadStageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowUpTypeRepository extends JpaRepository<FollowUpType, Integer> {

    List<FollowUpTypeView> findByIsDelFalseAndIsActiveTrue();
}
