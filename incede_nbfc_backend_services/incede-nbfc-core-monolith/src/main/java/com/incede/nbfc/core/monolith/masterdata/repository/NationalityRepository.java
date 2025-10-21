package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Nationality;
import com.incede.nbfc.core.monolith.masterdata.dto.NationalityView;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NationalityRepository extends JpaRepository<Nationality, Integer> {

    /**
     * Find all active nationalities
     *
     * @return List of active nationalities.
     */
    List<NationalityView> findByIsDelFalseAndIsActiveTrue();

    Optional<Nationality> findByIdentity(@NotNull(message = "nationality cannot be null") UUID nationality);

    @Query(value="SELECT a FROM Nationality a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<NationalityView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
