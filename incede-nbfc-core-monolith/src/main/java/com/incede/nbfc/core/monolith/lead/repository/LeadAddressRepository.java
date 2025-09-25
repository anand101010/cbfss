package com.incede.nbfc.core.monolith.lead.repository;

import com.incede.nbfc.core.monolith.lead.domain.entity.LeadAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadAddressRepository extends JpaRepository<LeadAddress, Integer> {

}
