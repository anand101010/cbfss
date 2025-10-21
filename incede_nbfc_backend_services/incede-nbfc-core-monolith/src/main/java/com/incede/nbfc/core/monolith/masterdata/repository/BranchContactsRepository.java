package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.BranchContacts;
import com.incede.nbfc.core.monolith.masterdata.dto.BranchContactView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchContactsRepository extends JpaRepository<BranchContacts, Integer> {

    @Query(value="Select ft from BranchContacts ft where ft.isDel=false AND" +
            " (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<BranchContactView> findByIsDelFalseByTenataId(Integer tenantId);
}
