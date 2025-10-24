package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressProofType;
import com.incede.nbfc.core.monolith.masterdata.dto.AddressProofTypeView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressProofTypeRepository extends JpaRepository<AddressProofType, Integer> {

    List<AddressProofTypeView> findByIsDelFalseAndIsActiveTrue();

    Optional<AddressProofType> findByIdentity( UUID addressProofType);
}
