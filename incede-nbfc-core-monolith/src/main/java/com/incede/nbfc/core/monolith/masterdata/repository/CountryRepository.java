package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CountryRepository extends JpaRepository<Countries, Integer> {
    List<Countries> findByCountryIdIn(Set<Integer> countryIds);
}
