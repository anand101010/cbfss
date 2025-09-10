package com.incede.nbfc.core.monolith.service;

import com.incede.nbfc.core.monolith.client.FinaVaultClient;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultRequestDto;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultService {
    private final FinaVaultClient finaVaultClient;

    @Value("${finaVault.security-token}")
    private String securityToken ;
    /**
     * Generates a Vault ID and returns a masked Aadhaar number using the external FinaVault service.
     *
     * @param uid Aadhaar number provided by the user
     * @return FinaVaultResponseDto containing Vault ID, masked UID, status, and additional response details
     */
    public FinaVaultResponseDto generateVaultIdAndMaskAadhaar(String uid) {
        try {
            FinaVaultRequestDto finaVaultRequest = new FinaVaultRequestDto();
            finaVaultRequest.setUid(uid);
            finaVaultRequest.setSecurityTokenVault(securityToken);
            return finaVaultClient.generateVaultIdAndMaskAadhaar(finaVaultRequest);
        }
        catch (Exception exception){
            log.error("Some errors in fine Vault", exception);
            throw new RuntimeException("Failed", exception);
        }
    }
}
