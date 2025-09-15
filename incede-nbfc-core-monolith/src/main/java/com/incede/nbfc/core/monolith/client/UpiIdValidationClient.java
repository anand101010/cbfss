package com.incede.nbfc.core.monolith.client;
import com.incede.nbfc.core.monolith.client.dto.UpiAccountDetailsResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.client.fallback.UpiIdValidationFallbackFactory;
import com.incede.nbfc.core.monolith.config.interceptors.DecentroClientInterceptorConfig;
import com.incede.nbfc.core.monolith.domain.dto.UpiAccountDetailsRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/**
 * UpiIdValidationClient that deals with the Upi id validation Client
 */

@FeignClient(name = "upiService", url = "${decentro.api.url}", configuration = DecentroClientInterceptorConfig.class, fallbackFactory = UpiIdValidationFallbackFactory.class)
public interface UpiIdValidationClient
{


    /**
     * ]
     * @param upiAccountDetailsRequestDto that will have accountdetails   with upiid details
     * @return validated account details
     */

    @PostMapping("${decentro.api.upiId-validate}")
    @CircuitBreaker(name = "post-api", fallbackMethod = "getValidatedUpiIdDetailsFallBack")
    UpiAccountDetailsResponseDto getValidatedUpiIdDetails (@RequestBody UpiAccountDetailsRequestDto upiAccountDetailsRequestDto);

    /**
     *
     * @param upiAccountDetailsRequestDto handle the exception
     * @param throwable
     * @return
     */
    default UpiAccountDetailsResponseDto getValidatedUpiIdDetailsFallBack(UpiAccountDetailsRequestDto upiAccountDetailsRequestDto, Throwable throwable){
        return FallBackHelper.FallbackResponse( new UpiAccountDetailsResponseDto(),throwable);
    }
}
