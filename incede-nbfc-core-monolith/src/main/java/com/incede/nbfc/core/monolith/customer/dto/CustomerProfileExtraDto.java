package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
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
public class CustomerProfileExtraDto {

    @NotNull(message = "Education level is required")
    private UUID educationLevelId;

    @NotNull(message = "Purpose is required")
    private UUID purposeId;



}
