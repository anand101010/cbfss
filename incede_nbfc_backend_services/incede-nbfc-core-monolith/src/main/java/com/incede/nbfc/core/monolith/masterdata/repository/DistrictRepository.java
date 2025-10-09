package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DistrictRepository extends JpaRepository<Districts, Integer> {
    List<Districts> findBydistrictIdIn(Set<Integer> districtIds);
}
