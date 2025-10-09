package com.incede.nbfc.core.monolith.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpiAccountDetailsRequestDto {

    @JsonProperty("reference_id")
    private String referenceId;
    @ToString.Exclude
    @JsonProperty("upi_vpa")
    private String upiVpa;
}
