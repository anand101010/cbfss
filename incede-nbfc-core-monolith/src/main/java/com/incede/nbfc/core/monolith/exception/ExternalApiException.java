package com.incede.nbfc.core.monolith.exception;

import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpResponse;
import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
    private final AadhaarOtpResponse errorResponse;

    public ExternalApiException(AadhaarOtpResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

}
