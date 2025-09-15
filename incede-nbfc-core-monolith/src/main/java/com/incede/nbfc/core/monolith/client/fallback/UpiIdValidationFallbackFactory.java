package com.incede.nbfc.core.monolith.client.fallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.client.UpiIdValidationClient;
import com.incede.nbfc.core.monolith.client.dto.UpiAccountDetailsResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpiIdValidationFallbackFactory implements FallbackFactory<UpiIdValidationClient> {

    private final ObjectMapper objectMapper ;

    @Override
    public UpiIdValidationClient create(Throwable cause) {
        return requestDto -> {
            UpiAccountDetailsResponseDto fallbackResponse = new UpiAccountDetailsResponseDto();

            if (cause instanceof FeignException fe) {
                try {
                    String body = fe.contentUTF8();
                    log.error("Vendor error response: {}", body);
                    return objectMapper.readValue(body, UpiAccountDetailsResponseDto.class);

                } catch (Exception e) {
                    log.error("Failed to parse vendor error response", e);
                    fallbackResponse.setStatus(CommonConstants.FAILURE);
                }
            } else {
                fallbackResponse.setStatus("FAILURE");
                fallbackResponse.setResponseCode("E99998");
                fallbackResponse.setMessage("Service unavailable. Please try again later.");
                fallbackResponse.setResponseKey("error_service_unavailable");
            }

            return fallbackResponse;
        };
    }
}
