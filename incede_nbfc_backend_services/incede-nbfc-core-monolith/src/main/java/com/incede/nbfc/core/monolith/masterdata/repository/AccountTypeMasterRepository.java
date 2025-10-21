package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountTypeMaster;
import com.incede.nbfc.core.monolith.masterdata.dto.AccountTypeMasterView;
import feign.Param;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountTypeMasterRepository extends JpaRepository<AccountTypeMaster,Integer> {

    List<AccountTypeMasterView> findByIsDelFalse();

    Optional<AccountTypeMaster> findByIdentity(  UUID accountType);

    @Query(value="SELECT a FROM AccountTypeMaster a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<AccountTypeMasterView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);

}
