package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.CustomerStatus;
import com.incede.nbfc.core.monolith.masterdata.dto.CustomerStatusView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerStatusRepository extends JpaRepository<CustomerStatus, Integer> {

    /****
     * active Customer Status.
     *
     * @return List of active Customer Status.
     */
    List<CustomerStatusView> findByIsDelFalseAndIsActiveTrue();

    Optional<CustomerStatus> findByIdentity( UUID customerStatus);
}
