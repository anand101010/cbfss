package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.SourceOfIncomeType;
import com.incede.nbfc.core.monolith.masterdata.dto.SourceOfIncomeTypeView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SourceOfIncomeTypeRepository extends JpaRepository<SourceOfIncomeType, Integer> {

    List<SourceOfIncomeTypeView> findByIsDelFalseAndIsActiveTrue();

    Optional<SourceOfIncomeType> findByIdentity(@NotNull(message = "Income Source ID is required") UUID incomeSourceId);
}
