package com.incede.nbfc.core.monolith.customer.repository;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerCategoryMappingRepository extends JpaRepository<CustomerCategoryMapping, Integer> {
    Optional<CustomerCategoryMapping> findByIdentity(UUID categoryId);
}
