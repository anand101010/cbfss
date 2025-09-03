package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAdditionalReferenceName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAdditionalReferenceNameRepository extends JpaRepository<CustomerAdditionalReferenceName,Integer> {
}
