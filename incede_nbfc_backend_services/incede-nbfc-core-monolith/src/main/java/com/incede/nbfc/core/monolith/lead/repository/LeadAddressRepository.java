package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadAddressRepository extends JpaRepository<LeadAddress, Integer> {

    Optional<LeadAddress> findByIdentity(UUID addressIdentity);
    List<LeadAddress> findByLead(Lead lead);
    List<LeadAddress> findByLeadAndIsDelFalse(Lead lead);


}
