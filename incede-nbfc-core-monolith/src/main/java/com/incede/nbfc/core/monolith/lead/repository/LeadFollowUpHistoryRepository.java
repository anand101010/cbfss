package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadFollowUpHistoryRepository extends JpaRepository<LeadFollowUpHistory, Integer>{
}
