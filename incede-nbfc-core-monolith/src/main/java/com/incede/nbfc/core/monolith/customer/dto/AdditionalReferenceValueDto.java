package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdditionalReferenceValueDto {
    private UUID referenceIdentity;
    private String referenceValue;
}
