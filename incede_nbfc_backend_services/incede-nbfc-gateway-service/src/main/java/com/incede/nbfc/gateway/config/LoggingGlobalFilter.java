package com.incede.nbfc.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * A global filter for logging all incoming HTTP requests and their responses in Spring Cloud Gateway.
 * <p>
 * This filter:
 * <ul>
 *   <li>Generates a unique request ID for each request.</li>
 *   <li>Logs the request path when the request starts.</li>
 *   <li>Logs the response status and total processing time after the response is sent.</li>
 * </ul>
 * <p>
 * Purpose: Provides a centralized logging mechanism for monitoring requests/responses
 * passing through the Gateway.
 */
@Slf4j
@Component
public class LoggingGlobalFilter implements GlobalFilter {


    /**
     * Intercepts each request passing through the Gateway.
     * <p>
     * - Logs request ID and path before forwarding.<br>
     * - Logs response status and time taken after the response is returned.
     *
     * @param exchange the current server exchange (contains request and response)
     * @param chain    the filter chain to continue processing
     * @return a reactive {@link Mono} that completes when the response is processed
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        log.info("Request [{}]: {}", requestId, requestPath);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Response [{}]: status={} for {} took {}ms",
                    requestId,
                    exchange.getResponse().getStatusCode(),
                    requestPath,
                    duration);
        }));
    }
}
