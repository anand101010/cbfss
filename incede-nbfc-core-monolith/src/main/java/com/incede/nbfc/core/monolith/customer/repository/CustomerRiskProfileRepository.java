package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerRiskProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRiskProfileRepository extends JpaRepository<CustomerRiskProfile,Integer> {
    Optional<CustomerRiskProfile> findByIdentity(UUID riskCategory);
}
