package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.BranchWeekSchedule;
import com.incede.nbfc.core.monolith.masterdata.dto.BranchWeekScheduleView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchWeekScheduleRepository extends JpaRepository<BranchWeekSchedule, Integer> {

    List<BranchWeekScheduleView> findByIsDelFalse();
}
