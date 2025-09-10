package com.incede.nbfc.core.monolith.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AadhaarOtpValidateRequestDto
{
    @JsonProperty("reference_id")
    private String referenceId;
    private Boolean consent;
    private String purpose;
    @NotBlank(message = "Initiation Transaction ID is mandatory")
    @JsonProperty("initiation_transaction_id")
    @ToString.Exclude
    private String initiationTransactionId;
    @ToString.Exclude
    @NotBlank(message = "OTP is mandatory")
    private String otp;

}
