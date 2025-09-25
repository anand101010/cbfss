package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerForm60RequestDto {

    private Long form60Id;

    @NotNull(message = "Customer ID is required")
    private Integer customerId;

    @NotNull(message = "Branch ID is required")
    private Integer branchId;

    private Integer pidDocumentId;

    private Integer addDocumentId;

    @DecimalMin(value = "0.01", inclusive = true, message = "Transaction amount must be greater than 0")
    private BigDecimal transactionAmount;

    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;

    @NotBlank(message = "Mode of transaction is required")
    @Size(max = 30, message = "Mode of transaction must not exceed 30 characters")
    private String modeOfTransaction;

    @Positive(message = "Number of persons must be positive")
    private Integer numberOfPersons = 1;

    @DecimalMin(value = "0.00", inclusive = true, message = "Agricultural income cannot be negative")
    private BigDecimal agriculturalIncome;

    @DecimalMin(value = "0.00", inclusive = true, message = "Other income cannot be negative")
    private BigDecimal otherIncome;

    @DecimalMin(value = "0.00", inclusive = true, message = "Taxable income cannot be negative")
    private BigDecimal taxableIncome;

    @DecimalMin(value = "0.00", inclusive = true, message = "Non-taxable income cannot be negative")
    private BigDecimal nonTaxableIncome;

    private LocalDate panCardApplicationDate;

    @Size(max = 20, message = "PAN card acknowledgement number must not exceed 20 characters")
    private String panCardApplicationAckNo;

    @Size(max = 50, message = "PID document number must not exceed 50 characters")
    private String pidDocumentNo;

    @Size(max = 50, message = "PID issuing authority must not exceed 50 characters")
    private String pidIssuingAuthority;

    @Size(max = 50, message = "Address document number must not exceed 50 characters")
    private String addDocumentNo;

    @Size(max = 50, message = "Address issuing authority must not exceed 50 characters")
    private String addIssuingAuthority;

    private LocalDate submissionDate;

    private Integer formFileId;

    @NotNull(message = "Created by is required")
    private Integer createdBy;

    private Integer updatedBy;
}
