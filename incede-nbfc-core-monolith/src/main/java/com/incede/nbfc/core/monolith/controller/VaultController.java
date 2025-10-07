package com.incede.nbfc.core.monolith.controller;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * VaultController handles requests related to Aadhaar vault operations.
 *
 * <p>This controller exposes endpoints that interact with the {@link VaultService}
 * to perform Aadhaar UID masking and tokenization. The primary use case is
 * to protect sensitive Aadhaar numbers by generating a vault ID and returning
 * a masked UID instead of exposing the actual Aadhaar number.</p>
 *
 */
@PreAuthorize("hasRole('STAFF')")
@RequestMapping("/ext/vault")
@Slf4j
@RequiredArgsConstructor
@RestController
public class VaultController {

    private final VaultService vaultService;

    /**
     * Endpoint to generate a vault ID and return a masked Aadhaar UID.
     *
     * @param uid Aadhaar number provided in the request query parameter
     * @return ResponseEntity containing {@link FinaVaultResponseDto} with vault ID and masked UID
     */
    @GetMapping("maskuid")
    public ResponseEntity<FinaVaultResponseDto> generateVaultIdAndMaskAadhaar(@RequestParam String uid){
        log.info("Aadhaar number received");
        FinaVaultResponseDto response =  vaultService.generateVaultIdAndMaskAadhaar(uid);
        return ResponseEntity.ok(response);
    }
}
