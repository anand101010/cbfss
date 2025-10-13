package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadAssignmentHistoryRepository extends JpaRepository<LeadAssignmentHistory, Integer> {

    Optional<LeadAssignmentHistory> findByLeadAndIsDelFalse(Lead lead);

    List<LeadAssignmentHistory> findAllByLeadAndIsDelFalse(Lead lead);
}
