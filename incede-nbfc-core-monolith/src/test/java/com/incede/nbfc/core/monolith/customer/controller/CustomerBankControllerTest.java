package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerBankAccountService;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerBankAccountControllerTest {

    @Mock
    private CustomerBankAccountService customerBankAccountService;

    @InjectMocks
    private CustomerBankAccountController customerBankAccountController;

    private UUID customerIdentity;
    private CustomerBankAccountRequestDto requestDto;
    private CustomerBankAccountResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerIdentity = UUID.randomUUID();

        CustomerBankAccountResponseDto.BankAccount bankAccount =
                CustomerBankAccountResponseDto.BankAccount.builder()
                        .bankAccountId(1)
                        .bankName("BankName")
                        .branchName("BranchName")
                        .ifscCode("IFSC0001")
                        .upiId("upi@bank")
                        .accountNumber("1234567890")
                        .maskedAccountNumber("XXXX7890")
                        .accountHolderName("John Doe")
                        .accountType(1)
                        .accountStatus("Active")
                        .isPrimary(true)
                        .pdStatus("OK")
                        .upiVerified(true)
                        .isActive(true)
                        .build();

        responseDto = CustomerBankAccountResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("customerCode")
                .status("status")
                .bankAccounts(List.of(bankAccount))
                .build();

        requestDto = CustomerBankAccountRequestDto.builder()
                .accountNumber("1234567890")
                .bankName("BankName")
                .ifscCode("IFSC0001")
                .isActive(true)
                .build();
    }


    MultipartFile mockFile = mock(MultipartFile.class);

    @Test
    void createBankAccount_shouldReturn201_whenValid() {
        when(customerBankAccountService.createBankAccount(any(UUID.class), anyString(), any(MultipartFile.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerBankAccountResponseDto> result =
                customerBankAccountController.createBankAccount(customerIdentity, "{}", mockFile);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void updateBankAccount_shouldReturn200_whenValid() {
        when(customerBankAccountService.updateBankAccount(any(UUID.class), anyInt(), any(CustomerBankAccountRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerBankAccountResponseDto> result =
                customerBankAccountController.updateBankAccount(customerIdentity, 1, requestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void getActiveBankAccounts_shouldReturn200_whenValid() {
        when(customerBankAccountService.getActiveBankAccounts(any(UUID.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerBankAccountResponseDto> result =
                customerBankAccountController.getActiveBankAccounts(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void createBankAccount_shouldPropagateBusinessException() {
        when(customerBankAccountService.createBankAccount(any(UUID.class), anyString(), any(MultipartFile.class)))
                .thenThrow(new BusinessException("Failed"));

        assertThrows(BusinessException.class, () ->
                customerBankAccountController.createBankAccount(customerIdentity, "{}", mockFile));
    }

    @Test
    void updateBankAccount_shouldPropagateNotFound() {
        when(customerBankAccountService.updateBankAccount(any(UUID.class), anyInt(), any(CustomerBankAccountRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("BankAccount", "1"));

        assertThrows(ResourceNotFoundException.class, () ->
                customerBankAccountController.updateBankAccount(customerIdentity, 1, requestDto));
    }

    @Test
    void getActiveBankAccounts_shouldPropagateException() {
        when(customerBankAccountService.getActiveBankAccounts(any(UUID.class)))
                .thenThrow(new RuntimeException("Service failed"));

        assertThrows(RuntimeException.class, () ->
                customerBankAccountController.getActiveBankAccounts(customerIdentity));
    }
}
