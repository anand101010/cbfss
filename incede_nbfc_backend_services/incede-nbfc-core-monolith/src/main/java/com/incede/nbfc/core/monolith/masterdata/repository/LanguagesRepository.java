package com.incede.nbfc.core.monolith.masterdata.repository;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Languages;
import com.incede.nbfc.core.monolith.masterdata.dto.LanguagesView;
import com.incede.nbfc.core.monolith.masterdata.dto.RelationshipsView;
import feign.Param;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LanguagesRepository extends JpaRepository<Languages, Integer> {


    List<LanguagesView> findByIsDelFalseAndIsActiveTrue();

    Optional<Languages> findByIdentity( UUID preferredLanguageId);

    @Query(value="SELECT a FROM Languages a WHERE a.isDel = false AND " +
            "(:tenantId IS NULL OR a.tenant.tenantId = :tenantId)")
    List<LanguagesView> findAllByTenantIdOrAll(@Param("tenantId") Integer tenantId);
}
