package com.incede.nbfc.core.monolith.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanvasserResponseDto {

    private String canvasserName;
    private String canvasserCode;
    private UUID canvasserIdentity;
}
 