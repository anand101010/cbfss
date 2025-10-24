package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerBankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerBankAccountController {

    public final CustomerBankAccountService customerBankAccountService;

    /**
     *
     * @param customerIdentity
     * @param bankAccountRequestJson
     * @param file
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/{customerIdentity}/bank-accounts")
    public ResponseEntity<CustomerBankAccountResponseDto> createBankAccount(
            @PathVariable UUID customerIdentity,
            @RequestPart("request") String bankAccountRequestJson,
            @RequestPart("file") MultipartFile file) {


        CustomerBankAccountResponseDto response = customerBankAccountService.createBankAccount(customerIdentity, bankAccountRequestJson,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing bank account for the given customer.
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{customerIdentity}/bank-accounts/{bankAccountId}")
    public ResponseEntity<CustomerBankAccountResponseDto> updateBankAccount(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID bankAccountId,
            @RequestBody @Valid CustomerBankAccountRequestDto requestDto) {

        CustomerBankAccountResponseDto response = customerBankAccountService.updateBankAccount(customerIdentity, bankAccountId, requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all active bank accounts for the given customer.
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{customerIdentity}/bank-accounts")
    public ResponseEntity<CustomerBankAccountResponseDto> getActiveBankAccounts(
            @PathVariable UUID customerIdentity) {

        CustomerBankAccountResponseDto response = customerBankAccountService.getActiveBankAccounts(customerIdentity);
        return ResponseEntity.ok(response);
    }

}
