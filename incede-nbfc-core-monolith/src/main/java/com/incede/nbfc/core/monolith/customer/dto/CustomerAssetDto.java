package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAssetDto {

    @NotNull(message = "Asset ID is required")
    @Min(value = 1, message = "Asset ID must be a positive number")
    private Integer assetId;

    @NotNull(message = "Asset Type ID is required")
    @Min(value = 1, message = "Asset Type ID must be a positive number")
    private Integer assetTypeId;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Approximate value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Approximate value must be greater than zero")
    private BigDecimal approxValue;

    private Boolean ownsAsset;

    @NotNull(message = "Has Home Loan flag is required")
    private Boolean hasHomeLoan;

    private String homeLoanCompany;


}
