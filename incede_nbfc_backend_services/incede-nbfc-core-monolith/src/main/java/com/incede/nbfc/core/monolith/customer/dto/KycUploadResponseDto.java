package com.incede.nbfc.core.monolith.customer.dto;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKycUpload;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class KycUploadResponseDto {
    private UUID identity;
    private String documentReference;
    private String fileName;
    private String fileType;
    private String filePath;
    private CustomerKycUpload.UploadStatus uploadStatus;
    private Integer version;
    private Timestamp uploadDate;
}
