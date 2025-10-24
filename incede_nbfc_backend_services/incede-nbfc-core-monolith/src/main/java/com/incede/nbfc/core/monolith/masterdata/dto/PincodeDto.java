package com.incede.nbfc.core.monolith.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PincodeDto {


    private String pincode;
    private String cityName;
    private String districtName;
    private String stateName;
    List<PostOfficesResponseDto> postOffices;
    private UUID identity;
}
