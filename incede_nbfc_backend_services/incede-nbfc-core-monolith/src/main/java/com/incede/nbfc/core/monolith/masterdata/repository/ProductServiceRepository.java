package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductService;
import com.incede.nbfc.core.monolith.masterdata.dto.ProductServiceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductServiceRepository extends JpaRepository<ProductService, Integer> {

    List<ProductServiceView> findByIsDelFalseAndIsActiveTrue();

    Optional<ProductService> findByIdentity( UUID interestedProductIdentity);

    @Query(value="SELECT a FROM ProductService a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<ProductServiceView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
