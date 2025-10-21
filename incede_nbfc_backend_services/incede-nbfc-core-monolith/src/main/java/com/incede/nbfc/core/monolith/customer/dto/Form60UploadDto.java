package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Form60UploadDto {

    private UUID form60Identity;

    @NotBlank(message ="docRefId cannot be null" )
    private String docRefId;

    @NotBlank(message ="fileName cannot be null" )
    @ToString.Exclude
    private String fileName;

    @NotBlank(message ="filePath cannot be null" )
    @ToString.Exclude
    private String filePath;


}
