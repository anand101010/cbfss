package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PostOfficesRepository extends JpaRepository<PostOffices, Integer> {
    List<PostOffices> findByPostOfficeIdIn(Set<Integer> postOfficesIds);
}
