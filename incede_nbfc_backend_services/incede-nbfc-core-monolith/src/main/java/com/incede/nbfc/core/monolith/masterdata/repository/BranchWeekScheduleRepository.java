package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.BranchWeekSchedule;
import com.incede.nbfc.core.monolith.masterdata.dto.BranchWeekScheduleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchWeekScheduleRepository extends JpaRepository<BranchWeekSchedule, Integer> {

    @Query(value="Select ft from BranchWeekSchedule ft where ft.isDel=false AND" +
            " (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<BranchWeekScheduleView> findByIsDelFalseByTenantId(Integer tenantId);
}
