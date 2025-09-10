package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPepDto {

    @NotBlank(message = "Status cannot be blank")
    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status;

    @NotNull(message = "Category ID is required")
    @Min(value = 1, message = "Category ID must be a positive number")
    private Integer categoryId;

    @NotNull(message = "Relationship ID is required")
    @Min(value = 1, message = "Relationship ID must be a positive number")
    private Integer relationshipId;

    @NotNull(message = "Verification Source ID is required")
    @Min(value = 1, message = "Verification Source ID must be a positive number")
    private Integer verificationSourceId;


    private Integer createdBy;

    private Integer updatedBy;
}
