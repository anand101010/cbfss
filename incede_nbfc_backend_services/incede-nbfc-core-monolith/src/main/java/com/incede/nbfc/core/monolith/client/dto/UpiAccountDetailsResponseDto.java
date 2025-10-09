package com.incede.nbfc.core.monolith.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpiAccountDetailsResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String decentroTxnId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;
    private String responseCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    private UpiIdBenificiaryDataDto UpiIdBenificiaryDataDto;
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseKey;


}
