package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerBankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerBankAccountControllerTest {

    @Mock
    private CustomerBankAccountService customerBankAccountService;

    @InjectMocks
    private CustomerBankAccountController controller;

    private UUID customerId;
    private UUID bankAccountId;
    private CustomerBankAccountRequestDto requestDto;
    private CustomerBankAccountResponseDto responseDto;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        bankAccountId = UUID.randomUUID();

        requestDto = CustomerBankAccountRequestDto.builder()
                .bankName("HDFC Bank")
                .branchName("MG Road")
                .ifscCode("HDFC0001234")
                .accountNumber("1234567890")
                .accountHolderName("John Doe")
                .build();

        CustomerBankAccountResponseDto.BankAccount bankAccount =
                CustomerBankAccountResponseDto.BankAccount.builder()
                        .bankName("HDFC Bank")
                        .branchName("MG Road")
                        .ifscCode("HDFC0001234")
                        .accountNumber("1234567890")
                        .maskedAccountNumber("****7890")
                        .accountHolderName("John Doe")
                        .isPrimary(true)
                        .isActive(true)
                        .build();

        responseDto = CustomerBankAccountResponseDto.builder()
                .identity(customerId)
                .customerCode("CUST001")
                .status("SUCCESS")
                .bankAccounts(Collections.singletonList(bankAccount))
                .build();
    }

    @Test
    void testCreateBankAccount() {
        when(customerBankAccountService.createBankAccount(customerId, requestDto))
                .thenReturn(responseDto);

        ResponseEntity<CustomerBankAccountResponseDto> response =
                controller.createBankAccount(customerId, requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(customerBankAccountService, times(1))
                .createBankAccount(customerId, requestDto);
    }

    @Test
    void testUpdateBankAccount() {
        when(customerBankAccountService.updateBankAccount(customerId, bankAccountId, requestDto))
                .thenReturn(responseDto);

        ResponseEntity<CustomerBankAccountResponseDto> response =
                controller.updateBankAccount(customerId, bankAccountId, requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(customerBankAccountService, times(1))
                .updateBankAccount(customerId, bankAccountId, requestDto);
    }

    @Test
    void testGetActiveBankAccounts() {
        when(customerBankAccountService.getActiveBankAccounts(customerId))
                .thenReturn(responseDto);

        ResponseEntity<CustomerBankAccountResponseDto> response =
                controller.getActiveBankAccounts(customerId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(customerBankAccountService, times(1))
                .getActiveBankAccounts(customerId);
    }
}