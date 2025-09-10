package com.incede.nbfc.core.monolith.client.fallback;

import com.incede.nbfc.core.monolith.client.dto.PhotoLivenessCheckResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
public class FallBackHelper
{

    /**
     * Helper method to create a fallback response for any DTO type.
     *
     * @param dtoInstance the DTO instance to populate
     * @param throwable the exception that triggered the fallback
     * @param <T> the type of the DTO
     * @return the populated DTO with failure status and message
     */
    public static <T> T FallbackResponse(T dtoInstance, Throwable throwable)
    {
        log.error("Fallback triggered: {}", throwable.getMessage(), throwable);
        String status = "FAILURE";
        String message;

        if (throwable instanceof ResponseStatusException ex)
        {
            String reason = ex.getReason() != null ? ex.getReason() : "Unknown error";
            message = "API Error: " + reason + " (status: " + ex.getStatusCode() + ")";
        }
        else
        {
            message = CommonConstants.FALLBACKMESSAGE;
        }
        try
        {
            dtoInstance.getClass().getMethod("setStatus", String.class).invoke(dtoInstance, status);
            dtoInstance.getClass().getMethod("setMessage", String.class).invoke(dtoInstance, message);
        }
        catch (Exception e)
        {
            log.error("Failed to set fallback fields: {}", e.getMessage(), e);
        }

        return dtoInstance;
    }


}
