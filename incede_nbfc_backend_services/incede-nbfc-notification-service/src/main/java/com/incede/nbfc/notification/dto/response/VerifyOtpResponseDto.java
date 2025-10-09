package com.incede.nbfc.notification.dto.response;

public record VerifyOtpResponseDto(
    String result,
    int attemptsRemaining
) {} 