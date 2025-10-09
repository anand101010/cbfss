package com.incede.nbfc.core.monolith.customer.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerKycResponseDto {
    private UUID identity;
    private String customerCode;
    private String firstName;
    private String lastName;
    private String dob;
    private UUID gender;
    private UUID customerStatus;
    private String onboardingStatus;
    private UUID branchId;
    private List<KycDocumentResponseDto> kycDocuments;
}

