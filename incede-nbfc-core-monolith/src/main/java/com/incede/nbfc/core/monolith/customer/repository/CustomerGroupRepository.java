package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, Integer> {
    Optional<CustomerGroup> findByIdentity(UUID customerGroupId);
}
