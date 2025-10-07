package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.ContactTypesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactTypesRepository extends JpaRepository<ContactTypes, Integer> {

    List<ContactTypesView> findByIsDelFalseAndIsActiveTrue();

    Optional<ContactTypes> findByIdentity( UUID contactType);

    Optional<ContactTypes> findByContactTypeAndIsActiveTrue(String mobile);
}

