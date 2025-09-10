package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import com.incede.nbfc.core.monolith.masterdata.dto.BranchesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchesRepository extends JpaRepository<Branches, Integer> {

    List<BranchesView> findByIsDelFalse();
}
