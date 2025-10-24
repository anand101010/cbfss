package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Form60UploadResponseDto {
    private UUID form60Identity;
    private UUID pdfDocRefId;
    private String fileName;
    private OffsetDateTime uploadedAt;
}
