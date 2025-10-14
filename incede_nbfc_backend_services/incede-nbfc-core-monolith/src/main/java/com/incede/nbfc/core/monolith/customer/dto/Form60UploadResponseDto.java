package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Form60UploadResponseDto {
    private UUID form60Identity;
    private String pdfDocRefId;
    private String filePath;
    private String fileName;
    private OffsetDateTime uploadedAt;
}
