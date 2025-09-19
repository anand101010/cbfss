package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DistrictRepository extends JpaRepository<Districts, Integer> {
    List<Districts> findBydistrictIdIn(Set<Integer> districtIds);
}
