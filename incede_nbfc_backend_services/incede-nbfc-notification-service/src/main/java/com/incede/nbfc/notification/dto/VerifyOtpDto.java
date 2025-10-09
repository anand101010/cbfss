package com.incede.nbfc.notification.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public record VerifyOtpDto(
    @NotBlank String code,
    OffsetDateTime updatedAt
) {} 