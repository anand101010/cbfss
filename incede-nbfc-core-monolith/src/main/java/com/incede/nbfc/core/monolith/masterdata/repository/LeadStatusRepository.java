package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadStatusRepository extends JpaRepository<LeadStatus, Integer> {

}
