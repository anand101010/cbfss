package com.incede.nbfc.core.monolith.customer.dto;

import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class KycUploadResponseDto {
    private UUID identity;
    private String documentReference;
    private String fileName;
    private String fileType;
    private String uploadStatus;
    private Integer version;
    private OffsetDateTime uploadDate;
}
