package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Designations;
import com.incede.nbfc.core.monolith.masterdata.dto.DesignationsView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignationsRepository extends JpaRepository<Designations, Integer> {

    Optional<Designations> findByIdentity(@NotNull(message = "Designation ID is required") UUID designationId);

    @Query(value="SELECT a FROM Designations a WHERE a.isActive = true AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<DesignationsView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
