package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.States;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.TaxCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface StatesRepository extends JpaRepository<States, Integer> {

    List<States> findByStateIdIn(Set<Integer> ids);
}
