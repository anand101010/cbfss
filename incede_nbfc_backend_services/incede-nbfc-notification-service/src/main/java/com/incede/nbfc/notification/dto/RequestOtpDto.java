package com.incede.nbfc.notification.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RequestOtpDto(
    @NotNull Integer tenantId,
    @NotBlank String branchCode,
    @NotNull Integer templateCatalogId,
    @NotNull Integer templateContentId,
    @NotNull String target,
    Integer customerIdentity,
    @Min(4) @Max(8) Integer length,
    @Min(60) @Max(900) Integer ttlSeconds,
    String context,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}