package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAdditionalReferenceNameResponseDto {
    @NotNull(message = "customerRefName is required")
    private String customerRefName;

    @NotNull(message = "Identity UUID is required")
    private UUID identity;

    private String valueType;

    private Boolean isActive;

    private Boolean isMandatory;

}
