package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.BranchContacts;
import com.incede.nbfc.core.monolith.masterdata.dto.BranchContactView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchContactsRepository extends JpaRepository<BranchContacts, Integer> {

    List<BranchContactView> findByIsDelFalse();
}
