package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.dto.AddressTypeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType, Integer> {

/**
 * Find all active address types.
 *
 * @return List of active address types
 */
    List<AddressTypeView> findByIsDelFalseAndIsActiveTrue();
    Optional<AddressType> findByAddressTypeNameAndIsDelFalse(String addressTypePermanent);

}
