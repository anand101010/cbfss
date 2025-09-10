package com.incede.nbfc.core.monolith.client;
import com.incede.nbfc.core.monolith.client.dto.AadhaarMaskingResponseDto;
import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.AadhaarMaskingClientFallbackFactory;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.config.interceptors.AadhaarMaskingClientInterceptorConfig;
import com.incede.nbfc.core.monolith.domain.dto.AadhaarMaskingRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

/**
 * Feign client which connect to pixl vendor service  those who are doing the aadhaar masking
 */
@FeignClient(name = "Aadhaar-Masking-service", url = "${pixl.api.url}", configuration = AadhaarMaskingClientInterceptorConfig.class,fallbackFactory = AadhaarMaskingClientFallbackFactory.class)
public interface AadhaarMaskingClient
{
    /**
     *
     * @paramAadhaarMaskingRequestDto these have json structure include base64 pdf format that will pass to the vendor service
     * @return AadhaarMaskingResponseDto  it return the masked  aadhar with proper reponse
     */

    @PostMapping("${pixl.api.aadhaar-mask}")
    @CircuitBreaker(name = "post-api", fallbackMethod = "getAadhaarMaskingFallBack")
    AadhaarMaskingResponseDto generateAadhaarMasked(@RequestBody AadhaarMaskingRequestDto aadhaarMaskingRequestDto);


    /**
     *
     * @param throwable will be parameter to get deceoder error
     * @return custom fallback response
     */
    default AadhaarMaskingResponseDto getAadhaarMaskingFallBack(Throwable throwable)
    {
        return FallBackHelper.FallbackResponse( new AadhaarMaskingResponseDto(),throwable);
    }

}
