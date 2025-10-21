package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AgentMaster;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AssetTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.AssetTypesView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgentMasterRepository extends JpaRepository<AgentMaster, Integer> {

    List<AgentMaster> findAllByAgentName(String canvasserName);
    List<AgentMaster> findAllByAgentNameStartingWithIgnoreCase(String agentName);

    Optional<AgentMaster> findByIdentity(UUID canvasserIdentity);
}