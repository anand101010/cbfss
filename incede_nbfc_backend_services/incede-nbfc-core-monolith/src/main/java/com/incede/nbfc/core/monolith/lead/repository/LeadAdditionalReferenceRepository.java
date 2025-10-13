package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAdditionalReference;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadAdditionalReferenceRepository extends JpaRepository<LeadAdditionalReference, Integer> {
    List<LeadAdditionalReference> findByLead(Lead lead);
    Optional<LeadAdditionalReference> findByLeadAndReferenceConfigId(Lead lead, AdditionalReferenceConfig config);


}
