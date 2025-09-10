package com.incede.nbfc.core.monolith.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KycPollingBoothDetails {
    private String latLong;
    private String name;
    private String nameVernacular;
    @ToString.Exclude
    private Integer number;

}
