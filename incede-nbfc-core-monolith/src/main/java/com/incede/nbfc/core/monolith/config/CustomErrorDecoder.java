package com.incede.nbfc.core.monolith.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Custom error decoder for Feign clients
 * Handles different HTTP error responses and converts them to appropriate exceptions
 */
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Feign client error for method: {}, status: {}, reason: {}",
                methodKey, response.status(), response.reason());

        switch (response.status()) {
            case 400:
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            case 401:
                return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            case 403:
                return new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
            case 404:
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
            case 429:
                return new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests");
            case 500:
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
            case 502:
                return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad Gateway");
            case 503:
                return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable");
            case 504:
                return new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Gateway Timeout");
            default:
                return FeignException.errorStatus(methodKey, response);
        }
    }
}
//}