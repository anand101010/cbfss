package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface PostOfficesRepository extends JpaRepository<PostOffices, Integer> {
    List<PostOffices> findByPostOfficeIdIn(Set<Integer> postOfficesIds);

    Optional<PostOffices> findByIdentity( UUID postOfficeId);
}
