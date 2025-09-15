package com.incede.nbfc.core.monolith.client;

import com.incede.nbfc.core.monolith.client.dto.FinaVaultRequestDto;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.config.FinaVaultFeignConfig;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * Feign client interface to communicate with the external FinaVault API.
 * This client is responsible for generating a vault ID and masking Aadhaar numbers.
 */
@FeignClient(name = "finaVaultClient", url = "${finaVault.api.url}",configuration = FinaVaultFeignConfig.class)
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
     *
     * @param throwable will be parameter to get deceoder error
     * @return custom fallback response
     */
    default FinaVaultResponseDto finaVaultFallback(Throwable throwable)
    {
        return FallBackHelper.FallbackResponse( new FinaVaultResponseDto(),throwable);
    }
}
