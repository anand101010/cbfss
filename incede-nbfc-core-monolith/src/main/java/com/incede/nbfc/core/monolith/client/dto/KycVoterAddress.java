package com.incede.nbfc.core.monolith.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KycVoterAddress {
    private Integer districtCode;
    private String districtName;
    private String districtNameVernacular;
    private String state;
    private String stateCode;
}
