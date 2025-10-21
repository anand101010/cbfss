package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.SourceOfIncomeType;
import com.incede.nbfc.core.monolith.masterdata.dto.SourceOfIncomeTypeView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SourceOfIncomeTypeRepository extends JpaRepository<SourceOfIncomeType, Integer> {

    List<SourceOfIncomeTypeView> findByIsDelFalseAndIsActiveTrue();

    Optional<SourceOfIncomeType> findByIdentity(@NotNull(message = "Income Source ID is required") UUID incomeSourceId);

    @Query(value="SELECT a FROM SourceOfIncomeType a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<SourceOfIncomeTypeView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
