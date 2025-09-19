package com.incede.nbfc.core.monolith.masterdata.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchTypeDto {

    private Integer branchTypeId;
    private String code;
    private String name;
    private String description;
    private Boolean isActive;
    private UUID identity;
}
