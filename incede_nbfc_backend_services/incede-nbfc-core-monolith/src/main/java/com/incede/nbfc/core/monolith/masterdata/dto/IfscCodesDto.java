package com.incede.nbfc.core.monolith.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IfscCodesDto {

    private String ifscCode;

    private String bankName;

    private String branchName;
    private String branchPlace;

    private Integer pincodes;

    private Boolean rbiFlag;
    private Boolean isActive;

    private UUID identity;
}
