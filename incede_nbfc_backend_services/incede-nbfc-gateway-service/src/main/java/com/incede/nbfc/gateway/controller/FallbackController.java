package com.incede.nbfc.gateway.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * FallbackController handles requests redirected by the gateway
 * when downstream microservices are unavailable or failing.
 * This is commonly used with circuit breaker mechanisms
 * to provide a graceful response instead of propagating errors to the client.
 */

@RestController
public class FallbackController {

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String serviceCode, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "unavailable");
        response.put("serviceCode", serviceCode);
        response.put("message", message);
        response.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/fallback/monolith")
    public ResponseEntity<Map<String, Object>> fallbackMonolith() {
        return createFallbackResponse("MONO-001", "Service is currently down. Please try again later.");
    }

    @RequestMapping("/fallback/notification")
    public ResponseEntity<Map<String, Object>> fallbackNotification() {
        return createFallbackResponse("NOTIF-002", "Service is currently down. Please try again later.");
    }
}
