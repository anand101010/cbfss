package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.BranchTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BranchTypeRepository extends JpaRepository<BranchTypes, Integer> {

    List<BranchTypes> findByBranchTypeIdIn(Set<Integer> branchTypeIds);

}
