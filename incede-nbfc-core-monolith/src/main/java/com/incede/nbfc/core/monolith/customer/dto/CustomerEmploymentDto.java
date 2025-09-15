package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEmploymentDto {

    @NotNull(message = "Occupation ID is required")
    @Min(value = 1, message = "Occupation ID must be a positive number")
    private Integer occupationId;

    @NotNull(message = "Designation ID is required")
    @Min(value = 1, message = "Designation ID must be a positive number")
    private Integer designationId;

    @NotBlank(message = "Employer name cannot be blank")
    @Size(max = 255, message = "Employer name must not exceed 255 characters")
    private String employer;

    @NotNull(message = "Income Source ID is required")
    @Min(value = 1, message = "Income Source ID must be a positive number")
    private Integer incomeSourceId;

    @NotNull(message = "Monthly salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly salary must be greater than 0")
    @Digits(integer = 12, fraction = 2, message = "Monthly salary must be a valid amount with up to 2 decimals")
    @ToString.Exclude
    private BigDecimal monthlySalary;

    @NotNull(message = "Annual income is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Annual income must be greater than 0")
    @Digits(integer = 12, fraction = 2, message = "Annual income must be a valid amount with up to 2 decimals")
    @ToString.Exclude
    private BigDecimal annualIncome;

    private Integer createdBy;

    private Integer updatedBy;
}
