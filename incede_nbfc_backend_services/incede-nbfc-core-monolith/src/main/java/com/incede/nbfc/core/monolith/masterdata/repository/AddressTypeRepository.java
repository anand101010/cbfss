package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.dto.AddressTypeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressTypeRepository extends JpaRepository<AddressType, Integer> {

/**
 * Find all active address types.
 *
 * @return List of active address types
 */

@Query(value="Select ft from AddressType ft where ft.isDel=false AND" +
        " ft.isActive=true AND (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<AddressTypeView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);
    Optional<AddressType> findByAddressTypeNameAndIsDelFalse(String addressTypePermanent);

    Optional<AddressType> findByIdentity( UUID addressType);
}
