package com.incede.nbfc.core.monolith.service;

import com.incede.nbfc.core.monolith.client.PennydropClient;
import com.incede.nbfc.core.monolith.client.dto.BankValidationRequestDto;
import com.incede.nbfc.core.monolith.client.dto.BankValidationResponseDto;
import com.incede.nbfc.core.monolith.domain.dto.BankValidateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BankValidationServiceTest {

    @Mock
    private PennydropClient pennydropClient;

    @InjectMocks
    private BankValidationService bankValidationService;

    private BankValidateRequestDto bankValidateRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Common test request DTO
        bankValidateRequestDto = new BankValidateRequestDto();
        bankValidateRequestDto.setAccountNumber("1234567890");
        bankValidateRequestDto.setIfsc("IFSC0001");
    }

    @Test
    @DisplayName("Should return response when PennydropClient validates successfully")
    void testValidateBankAccount_Success() {
        // Arrange
        BankValidationResponseDto mockResponse = new BankValidationResponseDto(
                "SUCCESS", "123", "1", "200", "Validation Successful",
                "John Doe", "TXN123", "Completed"
        );

        when(pennydropClient.validateBankAccount(any(BankValidationRequestDto.class)))
                .thenReturn(mockResponse);

        // Act
        BankValidationResponseDto response = bankValidationService.validateBankAccount(bankValidateRequestDto);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(response.getBeneficiaryName()).isEqualTo("John Doe");

        verify(pennydropClient, times(1)).validateBankAccount(any(BankValidationRequestDto.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when PennydropClient fails")
    void testValidateBankAccount_Failure() {
        // Arrange
        when(pennydropClient.validateBankAccount(any(BankValidationRequestDto.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        // Act & Assert
        try {
            bankValidationService.validateBankAccount(bankValidateRequestDto);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Bank account validation failed");
        }

        verify(pennydropClient, times(1)).validateBankAccount(any(BankValidationRequestDto.class));
    }
}
