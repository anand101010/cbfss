package com.incede.nbfc.core.monolith.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinaVaultRequestDto {
    @ToString.Exclude
    private String securityTokenVault;
    private String uid;
}
