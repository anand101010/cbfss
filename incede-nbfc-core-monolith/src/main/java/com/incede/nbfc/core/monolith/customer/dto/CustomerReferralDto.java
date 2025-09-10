package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReferralDto {

    @NotNull(message = "Referral source is required")
    @Min(value = 1, message = "Referral source ID must be a positive number")
    private Integer referralSourceId;

    @NotNull(message = "Canvassed type is required")
    @Min(value = 1, message = "Canvassed type ID must be a positive number")
    private Integer canvassedTypeId;

    @Min(value = 1, message = "Canvasser staff ID must be a positive number")
    private Integer canvasserStaffId;

    @ToString.Exclude
    private Integer createdBy;

    @ToString.Exclude
    private Integer updatedBy;
}
