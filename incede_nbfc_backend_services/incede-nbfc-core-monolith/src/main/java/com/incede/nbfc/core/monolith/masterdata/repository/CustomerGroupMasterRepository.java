package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CustomerGroupMaster;
import com.incede.nbfc.core.monolith.masterdata.dto.CustomerGroupMasterView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerGroupMasterRepository extends JpaRepository<CustomerGroupMaster, Integer> {

    List<CustomerGroupMasterView> findByIsDelFalseAndIsActiveTrue();

    Optional<CustomerGroupMaster> findByIdentity(UUID customerGroupId);
}
