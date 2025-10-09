package com.incede.nbfc.core.monolith.client;
import com.incede.nbfc.core.monolith.client.dto.BankValidationRequestDto;
import com.incede.nbfc.core.monolith.client.dto.BankValidationResponseDto;
import com.incede.nbfc.core.monolith.client.dto.UpiAccountDetailsResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.config.interceptors.DecentroClientInterceptorConfig;
import com.incede.nbfc.core.monolith.domain.dto.UpiAccountDetailsRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("${decentro.api.validate-bank}")
    BankValidationResponseDto validateBankAccount(@RequestBody BankValidationRequestDto bankvalidationrequestdto);

    /**
     *
     * @param BankValidationRequestDto handle the exception
     * @param throwable
     * @return
     */
    default BankValidationResponseDto getValidatedUpiIdDetailsFallBack(BankValidationRequestDto bankValidationRequestDto, Throwable throwable){
        return FallBackHelper.FallbackResponse( new BankValidationResponseDto(),throwable);
    }



}
