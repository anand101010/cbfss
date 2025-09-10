package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountStatuses;
import com.incede.nbfc.core.monolith.masterdata.dto.AccountStatusesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountStatusesRepository extends JpaRepository<AccountStatuses, Integer> {


    List<AccountStatusesView> findByIsDelFalseAndIsActiveTrue();
}
