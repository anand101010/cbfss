package com.incede.nbfc.core.monolith.client.fallback;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder
{
    private final ErrorDecoder defaultDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Feign client error for method: {}, status: {}, reason: {}",
                methodKey, response.status(), response.reason());
        try {
            String body = response.body() != null ? Util.toString(response.body().asReader(StandardCharsets.UTF_8)) : "";
            log.error("Feign error body: {}", body);
            return FeignException.errorStatus(methodKey, response);
        } catch (Exception e) {
            log.error("Error decoding feign exception", e);
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
