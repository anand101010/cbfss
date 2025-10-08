package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductService;
import com.incede.nbfc.core.monolith.masterdata.dto.LeadSourceView;
import com.incede.nbfc.core.monolith.masterdata.dto.ProductServiceView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductServiceRepository extends JpaRepository<ProductService, Integer> {

    List<ProductServiceView> findByIsDelFalseAndIsActiveTrue();

    Optional<ProductService> findByIdentity( UUID interestedProductIdentity);
}
