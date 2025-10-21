package com.incede.nbfc.core.monolith.customer.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Form60UploadResponseDto {
    private UUID form60Identity;
    @ToString.Exclude
    private String pdfDocRefId;
    @ToString.Exclude
    private String filePath;
    @ToString.Exclude
    private String fileName;
}
