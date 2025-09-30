package com.incede.nbfc.core.monolith.customer.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerKycRequestDto {

    @NotNull(message = "ID type is required")
    private UUID idType;

    @NotNull(message = "branchId  is required")

    private UUID branchId;
    @NotBlank(message = "ID number must not be blank")
    @ToString.Exclude
    @Size(max = 50, message = "ID number must not exceed 50 characters")
    private String idNumber;

    @Size(max = 100, message = "Place of issue must not exceed 100 characters")
    private String placeOfIssue;

    @Size(max = 100, message = "Issuing authority must not exceed 100 characters")
    private String issuingAuthority;

    @PastOrPresent(message = "Valid from date cannot be in the future")
    private LocalDate validFrom;

    @FutureOrPresent(message = "Valid to date must be today or in the future")
    private LocalDate validTo;

    @NotNull(message = "Verification status must be specified")
    private Boolean isVerified = false;

    @NotNull(message = "Active status must be specified")
    private Boolean isActive = true;

    private UUID tenantId;

    @NotBlank(message = "branchCode should not be blank")
    private String branchCode;



}
