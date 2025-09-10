package com.incede.nbfc.core.monolith.client;

import com.incede.nbfc.core.monolith.client.dto.AadhaarOtpResponse;
import com.incede.nbfc.core.monolith.client.dto.PhotoLivenessCheckResponseDto;
import com.incede.nbfc.core.monolith.client.fallback.FallBackHelper;
import com.incede.nbfc.core.monolith.config.interceptors.DecentroUATClientInterceptorConfig;
import com.incede.nbfc.core.monolith.domain.dto.PhotoLivenessRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

/**
 *  ForensicPhotoLivenessClient that will connect the image to feign to decentro site
 */
@FeignClient(name = "forensic-service", url = "${decentro.uat.url}", configuration = DecentroUATClientInterceptorConfig.class)
public interface ForensicPhotoLivenessClient
{
    /**
     *
     * @param photoLivenessRequestDto  this will set the photo liveness  will have the live image
     * @return  PhotoLivenessCheckResponseDto  after validation it will return  the validated percentage of photo
     */
    @PostMapping(value = "${decentro.uat.photo-liveness}")
    @CircuitBreaker(name = "post-api", fallbackMethod = "generatePhotoLivenessPercentageFallback")
    PhotoLivenessCheckResponseDto generatePhotoLivenessPercentage(@RequestBody PhotoLivenessRequestDto photoLivenessRequestDto);


    default PhotoLivenessCheckResponseDto generatePhotoLivenessPercentageFallback(Throwable throwable)
    {
        return FallBackHelper.FallbackResponse(new PhotoLivenessCheckResponseDto(),throwable);
    }
}