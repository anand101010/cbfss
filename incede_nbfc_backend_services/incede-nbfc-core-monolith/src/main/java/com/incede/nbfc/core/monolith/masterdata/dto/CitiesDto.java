package com.incede.nbfc.core.monolith.masterdata.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitiesDto {
    private Integer cityId;
    private String city;
    private Boolean isActive = true;
    private UUID identity;
}
