package com.incede.nbfc.core.monolith.customer.dto;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class KycDocumentResponseDto {
    private UUID identity;
    private String idType;
    private String idNumber;
    private String placeOfIssue;
    private String issuingAuthority;
    private String validFrom;
    private String validTo;
    private Boolean isVerified;
    private Boolean isActive;
    private List<KycUploadResponseDto> kycUploads;
}
