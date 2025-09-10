package com.incede.nbfc.core.monolith.client;

import com.incede.nbfc.core.monolith.client.dto.BankValidationRequestDto;
import com.incede.nbfc.core.monolith.client.dto.BankValidationResponseDto;
import com.incede.nbfc.core.monolith.client.dto.NameMatchResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.config.interceptors.DecentroClientInterceptorConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * {@code PennydropClient} is a Feign client for communicating with Decentro's
 * bank account validation (penny drop) API.
 * <p>
 * This client sends a request to validate a bank account using account number
 * and IFSC code, and handles failures gracefully using Resilience4j's
 * {@link CircuitBreaker}.
 * </p>
 */

@FeignClient(name = "bankValidationClient", url = "${decentro.api.url}", configuration = DecentroClientInterceptorConfig.class)
public interface PennydropClient {


    /**
     * Validates a bank account by sending account number and IFSC to Decentro API.
     *
     * @param bankvalidationrequestdto
     * @return
     */
    @PostMapping("${decentro.api.Validate-Bank}")
    @CircuitBreaker(name = "post-api",fallbackMethod = "validateBankAccount")
    BankValidationResponseDto validateBankAccount(@RequestBody BankValidationRequestDto bankvalidationrequestdto);

    /**
     * Fallback method for {@link #validateBankAccount(BankValidationRequestDto)}.
     * <p>
     * This method is triggered when the API call fails or when the circuit breaker is open.
     * It returns a fallback response with a default message.
     * </p>
     *
     * @param bankvalidationrequestdto the request DTO (used for logging/tracking).
     * @param throwable                the exception that triggered the fallback.
     * @return {@link BankValidationResponseDto} with fallback details.
     */

    default BankValidationResponseDto validateBankAccount(BankValidationRequestDto bankvalidationrequestdto,Throwable throwable){
        return FallBackHelper.FallbackResponse( new BankValidationResponseDto(),throwable);
    }

}
