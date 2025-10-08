package com.incede.nbfc.core.monolith.controller;

import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.controller.VaultController;
import com.incede.nbfc.core.monolith.service.VaultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link VaultController}.
 */
class VaultControllerTest {

    @Mock
    private VaultService vaultService;

    @InjectMocks
    private VaultController vaultController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for {@link VaultController#generateVaultIdAndMaskAadhaar(String)}.
     * Ensures the controller returns the response from the service correctly.
     */
    @Test
    void testGenerateVaultIdAndMaskAadhaar_success() {
        // Arrange
        String uid = "123456789012";
        FinaVaultResponseDto mockResponse = new FinaVaultResponseDto();
        mockResponse.setStatus("SUCCESS");
        mockResponse.setUidForDisplay("XXXXXXXX9012");
        mockResponse.setUid("123456789012");
        mockResponse.setUidReferenceKey("vault-key-001");

        when(vaultService.generateVaultIdAndMaskAadhaar(uid)).thenReturn(mockResponse);

        // Act
        ResponseEntity<FinaVaultResponseDto> responseEntity =
                vaultController.generateVaultIdAndMaskAadhaar(uid);

        // Assert
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo("SUCCESS");
        assertThat(responseEntity.getBody().getUidForDisplay()).isEqualTo("XXXXXXXX9012");
        assertThat(responseEntity.getBody().getUidReferenceKey()).isEqualTo("vault-key-001");
    }
}
