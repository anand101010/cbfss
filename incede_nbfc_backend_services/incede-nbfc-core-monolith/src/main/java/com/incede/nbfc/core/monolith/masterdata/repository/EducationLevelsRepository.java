package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.EducationLevels;
import com.incede.nbfc.core.monolith.masterdata.dto.EducationLevelsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EducationLevelsRepository extends JpaRepository<EducationLevels, Integer> {

    List<EducationLevelsView> findByIsDelFalseAndIsActiveTrue();

    Optional<EducationLevels> findByIdentity( UUID educationLevelId);

    @Query(value="SELECT a FROM EducationLevels a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<EducationLevelsView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
