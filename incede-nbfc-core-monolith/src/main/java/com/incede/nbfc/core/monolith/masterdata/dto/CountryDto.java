package com.incede.nbfc.core.monolith.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {

    private Integer countryId;
    private String country;
    private Boolean isActive;
    private UUID identity;
}
