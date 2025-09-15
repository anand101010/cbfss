package com.incede.nbfc.core.monolith.controller;
import com.incede.nbfc.core.monolith.client.dto.BankValidationResponseDto;
import com.incede.nbfc.core.monolith.domain.dto.BankValidateRequestDto;
import com.incede.nbfc.core.monolith.service.BankValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller exposes endpoints for validating bank account details
 *  (such as account number and IFSC) through an external service,
 *  typically using a "penny drop" mechanism to confirm account validity.
 */
@RestController
@RequestMapping("/ext/bank/")
@Slf4j
public class BankValidationController {

    private final BankValidationService bankvalidationservice;

    public BankValidationController(BankValidationService bankvalidationservice)
    {
        this.bankvalidationservice = bankvalidationservice;
    }

    /**
     * Validates the given bank account details against an external service.
     *
     * @param bankvalidaterequestdto the request containing account number and IFSC
     * @return ResponseEntity containing validation result
     */
    @PostMapping("account/verify")
    public ResponseEntity<BankValidationResponseDto> validateBankAccount(@RequestBody BankValidateRequestDto bankvalidaterequestdto){
        BankValidationResponseDto response = bankvalidationservice.validateBankAccount(bankvalidaterequestdto);
        return ResponseEntity.ok(response);
    }
}
