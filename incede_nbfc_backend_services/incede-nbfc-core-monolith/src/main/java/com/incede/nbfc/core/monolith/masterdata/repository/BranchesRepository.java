package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BranchesRepository extends JpaRepository<Branches, Integer> {

    @Query(value="Select ft from Branches ft where ft.isDel=false AND" +
            " (:tenantId IS NULL OR ft.tenantId = :tenantId) ")
    List<Branches> findAllBranchesByIsDelFalseByTenantId(Integer tenantId);

    Optional<Branches> findByIdentity(UUID branchId);

    Optional<Branches> findByBranchId(Integer branchId);
}
