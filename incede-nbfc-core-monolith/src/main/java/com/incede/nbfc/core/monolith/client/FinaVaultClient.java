package com.incede.nbfc.core.monolith.client;

import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpResponse;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultRequestDto;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * Feign client interface to communicate with the external FinaVault API.
 * This client is responsible for generating a vault ID and masking Aadhaar numbers.
 */
@FeignClient(name = "finaVaultClient", url = "${finaVault.api.url}")
public interface FinaVaultClient {
    /**
     * Calls the FinaVault service "/add" endpoint to generate a vault ID
     * and mask the provided Aadhaar number.
     * @param finaVaultRequestdto Request object containing UID and security token.
     * @return FinaVaultResponseDto Response object containing vault ID, masked Aadhaar, and status info.
     */
    @PostMapping("/add")
    @CircuitBreaker(name = "post-api", fallbackMethod = "finaVaultFallback")
    FinaVaultResponseDto generateVaultIdAndMaskAadhaar(@RequestBody FinaVaultRequestDto finaVaultRequestdto);

    /**
     * Fallback method for the CircuitBreaker when the main API call fails.
     * This method ensures the system remains resilient during service downtime or errors.
     *
     * @param throwable The exception that caused the fallback (could be timeout, service unavailable, etc.)
     * @return FinaVaultResponseDto A fallback response with failure status and error message.
     */
    default FinaVaultResponseDto finaVaultFallback(Throwable throwable) {
        return FallBackHelper.FallbackResponse( new FinaVaultResponseDto(),throwable);
    }
}
