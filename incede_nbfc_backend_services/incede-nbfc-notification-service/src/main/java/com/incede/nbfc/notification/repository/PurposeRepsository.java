package com.incede.nbfc.notification.repository;

import com.incede.nbfc.notification.domain.Channel;
import com.incede.nbfc.notification.domain.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurposeRepsository extends JpaRepository<Purpose, Integer>
{
    @Query("""
        SELECT p.name
        FROM TemplateCatalog tc
        JOIN Purpose p ON tc.category = p.purposeId AND tc.tenantId = p.tenantId
        WHERE tc.templateCatalogId = :templateCatalogId
    """)
    Optional<String> findPurposeNameByTemplateCatalogId(@Param("templateCatalogId") Integer templateCatalogId);

}
