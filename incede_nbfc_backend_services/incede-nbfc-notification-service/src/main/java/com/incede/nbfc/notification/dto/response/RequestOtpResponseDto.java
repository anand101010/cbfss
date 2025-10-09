package com.incede.nbfc.notification.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RequestOtpResponseDto(
    UUID requestId,
    OffsetDateTime expiresAt,
    String status
) {} 