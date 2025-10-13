package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Cities;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CitiesRepository extends JpaRepository<Cities,Integer> {
    List<Cities> findByCityIdIn(Set<Integer> postOfficesIds);

    Optional<Cities> findByCity(String district);
}
