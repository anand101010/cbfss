package com.incede.nbfc.core.monolith.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PincodeDto {

    private Integer pincodeId;
    private Integer stateId;
    private String stateName;
    private Integer districtId;
    private String districtName;
    private Integer cityId;
    private String cityName;
    private Integer pincode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private UUID identity;

    private StatesDto stateDto;
    private CitiesDto CitiesDto;
    private DistrictDto DistrictDto;
}
