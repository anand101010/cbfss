package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PostOfficesRepository extends JpaRepository<PostOffices, Integer> {
    List<PostOffices> findByPostOfficeIdIn(Set<Integer> postOfficesIds);

    Optional<PostOffices> findByIdentity( UUID postOfficeId);

    List<PostOffices> findByPincode_PincodeIdIn(List<Integer> pincodeIds);
}
