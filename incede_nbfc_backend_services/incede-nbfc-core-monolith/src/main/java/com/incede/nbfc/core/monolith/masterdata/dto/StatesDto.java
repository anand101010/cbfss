package com.incede.nbfc.core.monolith.masterdata.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatesDto {

    private Integer stateId;
    private String state;
    private Boolean isActive;
    private UUID identity;
}
