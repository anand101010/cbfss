package com.incede.nbfc.notification.repository;

import com.incede.nbfc.notification.domain.TemplateCatalog;
import com.incede.nbfc.notification.domain.TemplateContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TemplateContentsRepository   extends JpaRepository<TemplateContents, Integer>
{


    @Query("""
        SELECT tc.body
        FROM TemplateContents tc
        WHERE tc.template.tenantId = :tenantId
          AND tc.template.templateCatalogId = :templateCatalogId
          AND tc.purpose.purposeId = :purposeId
          AND tc.channel.channelId = :channelId
          AND tc.isActive = true
          AND tc.isDelete = false
          AND tc.template.isActive = true
          AND tc.template.isDelete = false
    """)
    Optional<String> findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(
            @Param("tenantId") Integer tenantId,
            @Param("templateCatalogId") Integer templateCatalogId,
            @Param("purposeId") Integer purposeId,
            @Param("channelId") Integer channelId
    );

    @Modifying
    @Query(
            value = "INSERT INTO notification.template_contents (" +
                    "template_catalog_id, channel, language_code, subject, body, footer, max_length, sender_id, " +
                    "provider_params, dlt_principal_id, dlt_template_id, checksum, is_active, " +
                    "created_by, created_at, updated_by, updated_at, is_delete, identity" +
                    ") VALUES (" +
                    ":templateCatalogId, :channel, :languageCode, :subject, :body, :footer, :maxLength, :senderId, " +
                    "CAST(:providerParams AS JSONB), :dltPrincipalId, :dltTemplateId, :checksum, :isActive, " +
                    ":createdBy, :createdAt, :updatedBy, :updatedAt, :isDel, :identity" +
                    ")",
            nativeQuery = true
    )
    void saveNative(
            @Param("templateCatalogId") Integer templateCatalogId,
            @Param("channel") String channel,
            @Param("languageCode") String languageCode,
            @Param("subject") String subject,
            @Param("body") String body,
            @Param("footer") String footer,
            @Param("maxLength") Integer maxLength,
            @Param("senderId") String senderId,
            @Param("providerParams") String providerParams,   // JSON string
            @Param("dltPrincipalId") String dltPrincipalId,
            @Param("dltTemplateId") String dltTemplateId,
            @Param("checksum") String checksum,
            @Param("isActive") Boolean isActive,
            @Param("createdBy") Integer createdBy,
            @Param("createdAt") OffsetDateTime createdAt,
            @Param("updatedBy") Integer updatedBy,
            @Param("updatedAt") OffsetDateTime updatedAt,
            @Param("isDel") Boolean isDel,
            @Param("identity") UUID identity
    );

    Optional<TemplateContents> findByIdentityAndTenantIdAndIsActiveTrueAndIsDeleteFalse(
            UUID identity,
            Integer tenantId
    );


}
