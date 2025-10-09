package com.incede.nbfc.core.monolith.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AadhaarOtpRequestDto
{

    @JsonProperty("reference_id")
    private String referenceId;
    private Boolean consent;
    private String purpose;
    @NotBlank(message = "OTP is mandatory")
    @ToString.Exclude
    @JsonProperty("aadhaar_number")
    private String aadhaarNumber;
}
