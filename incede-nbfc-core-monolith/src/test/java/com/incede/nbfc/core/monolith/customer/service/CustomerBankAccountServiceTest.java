package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.controller.CustomerBankAccountController;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerBankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerBankAccountControllerTest {

    @Mock
    private CustomerBankAccountService service;

    @InjectMocks
    private CustomerBankAccountController controller;

    private UUID identity;
    private CustomerBankAccountResponseDto.BankAccount bankAccountDto;
    private CustomerBankAccountResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        identity = UUID.randomUUID();
        bankAccountDto = CustomerBankAccountResponseDto.BankAccount.builder()
                .bankAccountId(1)
                .accountNumber("1234567890")
                .maskedAccountNumber("XXXX7890")
                .isPrimary(true)
                .accountStatus("Active")
                .build();
        responseDto = new CustomerBankAccountResponseDto(identity, "customerCode", "status", List.of(bankAccountDto));
    }

    @Test
    void createBankAccount_shouldReturn201_whenValid() {
        when(service.createBankAccount(any(UUID.class), anyString(), any(MultipartFile.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerBankAccountResponseDto> response =
                controller.createBankAccount(identity, "{}", mock(MultipartFile.class));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void createBankAccount_shouldPropagateBusinessException() {
        when(service.createBankAccount(any(UUID.class), anyString(), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Failed"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> controller.createBankAccount(identity, "{}", mock(MultipartFile.class)));

        assertEquals("Failed", ex.getMessage());
    }
}
