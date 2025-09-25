package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAssetDto {


    @NotNull(message = "Asset Type ID is required")
    private UUID assetTypeId;


    private Boolean ownsAsset;

    @NotNull(message = "Has Home Loan flag is required")
    private Boolean hasHomeLoan;

    private String homeLoanCompany;


}
