package com.incede.nbfc.core.monolith.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PincodeRowDTO {
    private String pincode;
    private String officeName;
    private String district;
    private String region;
    private String division;
    private String deliveryStatus;
    private String state;
    private String officeType;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
