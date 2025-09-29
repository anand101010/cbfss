package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant,Integer> {


    Optional<Tenant> findByIdentity(UUID referenceValueIdentity);
}
