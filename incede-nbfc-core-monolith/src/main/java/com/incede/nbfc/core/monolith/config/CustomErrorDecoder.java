package com.incede.nbfc.core.monolith.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.client.dto.GenericFeignErrorDto;
import com.incede.nbfc.core.monolith.exception.FeignCustomException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Custom error decoder for Feign clients
 * Handles different HTTP error responses and converts them to appropriate exceptions
 */

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder
{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Feign error at {} - status: {}, reason: {}", methodKey, response.status(), response.reason());

        if (response.body() == null) {
            Map<String, Object> fallbackMap = Map.of(
                    "status", "FAILURE",
                    "httpStatus", response.status(),
                    "message", response.reason()
            );
            return new FeignCustomException(fallbackMap);
        }

        try (InputStream is = response.body().asInputStream()) {
            String errorBody = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            log.debug("Raw Feign error body: {}", errorBody);


            Map<String, Object> errorMap = objectMapper.readValue(errorBody, Map.class);

            return new FeignCustomException(errorMap);

        } catch (IOException e) {
            log.error("Error parsing Feign response body", e);
            Map<String, Object> fallbackMap = Map.of(
                    "status", "FAILURE",
                    "httpStatus", response.status(),
                    "message", "Unable to parse error body"
            );
            return new FeignCustomException(fallbackMap);
        }
    }

}