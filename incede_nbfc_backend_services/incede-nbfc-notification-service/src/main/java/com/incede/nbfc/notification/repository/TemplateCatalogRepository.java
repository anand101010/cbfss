package com.incede.nbfc.notification.repository;

import com.incede.nbfc.notification.domain.OtpRequest;
import com.incede.nbfc.notification.domain.TemplateCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TemplateCatalogRepository  extends JpaRepository<TemplateCatalog, Integer>
{

    @Query("""
        SELECT c.name
        FROM TemplateCatalog tc
        JOIN Channel c ON tc.channel = c.channelId AND tc.tenantId = c.tenantId
        WHERE tc.templateCatalogId = :templateCatalogId
    """)
    Optional<String> findChannelNameByTemplateCatalogId(@Param("templateCatalogId") Integer templateCatalogId);

    Optional<TemplateCatalog> findByIdentityAndTenantIdAndIsActiveTrueAndIsDeleteFalse(
            UUID identity,
            Integer tenantId
    );
}
