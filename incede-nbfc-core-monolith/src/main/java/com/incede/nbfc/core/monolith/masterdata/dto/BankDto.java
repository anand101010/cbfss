package com.incede.nbfc.core.monolith.masterdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDto {
    private String bankName;
    private Boolean isActive;
    private UUID identity;
}
