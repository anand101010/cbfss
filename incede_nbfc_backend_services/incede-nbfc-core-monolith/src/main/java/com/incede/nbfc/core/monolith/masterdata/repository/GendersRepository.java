package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Genders;
import com.incede.nbfc.core.monolith.masterdata.dto.GendersView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GendersRepository extends JpaRepository<Genders, Integer> {

    /**
     * Find all active genders
     *
     * @return List of active Customer Status.
     */
    List<GendersView> findByIsDelFalseAndIsActiveTrue();

    Optional<Genders> findByIdentity( UUID gender);

    @Query(value="SELECT a FROM Genders a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<GendersView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
