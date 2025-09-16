package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileExtraDto {

    @NotNull(message = "Education level is required")
    @Min(value = 1, message = "Education level ID must be a positive number")
    private Integer educationLevelId;

    @NotNull(message = "Purpose is required")
    @Min(value = 1, message = "Purpose ID must be a positive number")
    private Integer purposeId;



}
