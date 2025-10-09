package com.incede.nbfc.core.monolith.service;

import com.incede.nbfc.core.monolith.client.FinaVaultClient;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultRequestDto;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VaultServiceTest {

    @Mock
    private FinaVaultClient finaVaultClient; // Mock external dependency (client)

    @InjectMocks
    private VaultService vaultService; // Service under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inject value into private field annotated with @Value in VaultService
        ReflectionTestUtils.setField(vaultService, "securityToken", "dummy-security-token");
    }

    @Test
    void testGenerateVaultIdAndMaskAadhaar_success() {
        // Arrange: prepare test data and mock client response
        String uid = "123456789012";
        FinaVaultResponseDto mockResponse = new FinaVaultResponseDto();
        mockResponse.setStatus("SUCCESS");
        mockResponse.setUidForDisplay("XXXX-XXXX-9012");
        mockResponse.setUid("123456789012");
        mockResponse.setUidReferenceKey("ref123");

        when(finaVaultClient.generateVaultIdAndMaskAadhaar(any(FinaVaultRequestDto.class)))
                .thenReturn(mockResponse);

        // Act: call the method under test
        FinaVaultResponseDto result = vaultService.generateVaultIdAndMaskAadhaar(uid);

        // Assert: verify returned response is correct
        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("XXXX-XXXX-9012", result.getUidForDisplay());

        // Verify: check that the mock client was called once
        verify(finaVaultClient, times(1))
                .generateVaultIdAndMaskAadhaar(any(FinaVaultRequestDto.class));
    }

    @Test
    void testGenerateVaultIdAndMaskAadhaar_failure() {
        // Arrange: mock client to throw exception
        String uid = "987654321098";

        when(finaVaultClient.generateVaultIdAndMaskAadhaar(any(FinaVaultRequestDto.class)))
                .thenThrow(new RuntimeException("Service down"));

        // Act & Assert: expect vaultService to wrap/throw an exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vaultService.generateVaultIdAndMaskAadhaar(uid);
        });

        assertTrue(exception.getMessage().contains("Failed")); // check error message
        verify(finaVaultClient, times(1))
                .generateVaultIdAndMaskAadhaar(any(FinaVaultRequestDto.class));
    }
}
