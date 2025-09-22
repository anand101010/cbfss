package com.incede.nbfc.core.monolith.customer.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class CkycRequestDto {

    private String dateTime;
    @ToString.Exclude
    private String idNo;
    @ToString.Exclude
    private String idType;
}

