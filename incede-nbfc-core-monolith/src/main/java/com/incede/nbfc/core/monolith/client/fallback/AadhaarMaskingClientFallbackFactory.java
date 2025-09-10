package com.incede.nbfc.core.monolith.client.fallback;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.client.AadhaarMaskingClient;
import com.incede.nbfc.core.monolith.client.dto.AadhaarMaskingResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AadhaarMaskingClientFallbackFactory  implements FallbackFactory<AadhaarMaskingClient>
{


    private final ObjectMapper objectMapper;

    @Override
    public AadhaarMaskingClient create(Throwable cause) {
        return requestDto -> {
            AadhaarMaskingResponseDto fallbackResponse = new AadhaarMaskingResponseDto();
            if (cause instanceof FeignException fe)
            {
                try {
                    String body = fe.contentUTF8();
                    log.error("Vendor error response: {}", body);
                    return objectMapper.readValue(body, AadhaarMaskingResponseDto.class);
                }
                catch (Exception e)
                {
                    log.error("Failed to parse vendor error response", e);
                    fallbackResponse.setMessage("FAILURE - Fallback response");
                    fallbackResponse.setImageUuid(null);
                    fallbackResponse.setAadhaarDetected(false);
                    fallbackResponse.setAadhaarMasked(false);
                    fallbackResponse.setNumberOfPages(0);
                    fallbackResponse.setUtcTimeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss")));
                    fallbackResponse.setResponseImage(null);
                }
            }
            else
            {
                fallbackResponse.setMessage("FAILURE - Fallback response");
                fallbackResponse.setImageUuid(null);
                fallbackResponse.setAadhaarDetected(false);
                fallbackResponse.setAadhaarMasked(false);
                fallbackResponse.setNumberOfPages(0);
                fallbackResponse.setUtcTimeStamp(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss")));
                fallbackResponse.setResponseImage(null);
            }

            return fallbackResponse;
        };
    }





}
