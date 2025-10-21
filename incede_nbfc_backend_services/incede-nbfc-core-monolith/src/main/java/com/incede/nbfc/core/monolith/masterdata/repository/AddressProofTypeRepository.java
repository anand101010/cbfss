package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressProofType;
import com.incede.nbfc.core.monolith.masterdata.dto.AddressProofTypeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressProofTypeRepository extends JpaRepository<AddressProofType, Integer> {

    @Query(value="Select ft from AddressProofType ft where ft.isDel=false AND" +
            " (:tenantId IS NULL OR ft.tenant.tenantId = :tenantId) ")
    List<AddressProofTypeView> findByIsDelFalseAndIsActiveTrueByTenantId(Integer tenantId);

    Optional<AddressProofType> findByIdentity( UUID addressProofType);
}
