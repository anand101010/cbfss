package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerBankAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerBankAccountController {

    public final CustomerBankAccountService customerBankAccountService;

    /**
     *Mapping Customer Bank Account Details
     * @param customerIdentity
     * @param customerBankAccountRequestDto
     * @return
     */
    @PostMapping("/{customerIdentity}/bank-accounts")
    public ResponseEntity<CustomerBankAccountResponseDto> createBankAccount(
            @PathVariable UUID customerIdentity,
            @RequestBody CustomerBankAccountRequestDto  customerBankAccountRequestDto) {


        CustomerBankAccountResponseDto response = customerBankAccountService.createBankAccount(customerIdentity,customerBankAccountRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing bank account for the given customer.
     * @param customerIdentity
     * @param bankAccountId
     * @param requestDto
     * @return
     */
    @PutMapping("/{customerIdentity}/bank-accounts/{bankAccountId}")
    public ResponseEntity<CustomerBankAccountResponseDto> updateBankAccount(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID bankAccountId,
            @RequestBody @Valid CustomerBankAccountRequestDto requestDto) {

        CustomerBankAccountResponseDto response = customerBankAccountService.updateBankAccount(customerIdentity, bankAccountId, requestDto);
        return ResponseEntity.ok(response);
    }
    /**
     * Get all active bank accounts for a given customer identity.
     *
     * @param identity UUID of the customer
     * @return CustomerBankAccountResponseDto containing active accounts
     */
    @GetMapping("/{identity}/bank-accounts/active")
    public ResponseEntity<CustomerBankAccountResponseDto> getActiveBankAccounts(@PathVariable UUID identity) {
        CustomerBankAccountResponseDto response = customerBankAccountService.getActiveBankAccounts(identity);
        return ResponseEntity.ok(response);
    }


}