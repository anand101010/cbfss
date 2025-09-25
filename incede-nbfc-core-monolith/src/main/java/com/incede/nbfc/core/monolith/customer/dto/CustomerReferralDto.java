package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReferralDto {

    @NotNull(message = "Referral source is required")
    private UUID referralSourceId;

    @NotNull(message = "Canvassed type is required")
    private UUID canvassedTypeId;

    private Integer canvasserStaffId;


}
