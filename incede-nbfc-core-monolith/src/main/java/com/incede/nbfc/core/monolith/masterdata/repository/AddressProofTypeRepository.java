package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressProofType;
import com.incede.nbfc.core.monolith.masterdata.dto.AddressProofTypeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressProofTypeRepository extends JpaRepository<AddressProofType, Integer> {

    List<AddressProofTypeView> findByIsDelFalseAndIsActiveTrue();
}
