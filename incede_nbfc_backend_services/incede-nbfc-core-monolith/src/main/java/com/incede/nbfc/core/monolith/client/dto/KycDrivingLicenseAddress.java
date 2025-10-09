package com.incede.nbfc.core.monolith.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KycDrivingLicenseAddress {
    private String addressLine;
    private String completeAddress;
    private String country;
    private String district;
    @ToString.Exclude
    private String pin;
    private String state;
    private String type;
}


