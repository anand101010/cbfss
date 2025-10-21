package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerNotificationPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor

@Tag(name = "Customer Notification Preference API", description = "APIs for managing customer notification preferences")
public class CustomerNotificationPreferenceController {

    private final CustomerNotificationPreferenceService notificationService;

    /**
     * Create notification preferences for a customer.
     *
     * @param customerId Customer UUID
     * @param request    DTO containing notification preference details
     * @return ResponseEntity with created notification preferences
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/{customerId}/notification-preferences")
    @Operation(summary = "Create Notification Preference", description = "Creates notification preferences for a customer")
    public ResponseEntity<CustomerNotificationPreferenceResponseDto> createNotificationPreference(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerNotificationPreferenceRequestDto request) {

        CustomerNotificationPreferenceResponseDto response = notificationService.saveNotification(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update notification preferences for a customer.
     *
     * @param customerId Customer UUID
     * @param request    DTO containing updated notification preferences
     * @return ResponseEntity with updated notification preferences
     */
    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{customerId}/notification-preferences")
    @Operation(summary = "Update Notification Preference", description = "Updates notification preferences for a customer")
    public ResponseEntity<CustomerNotificationPreferenceResponseDto> updateNotificationPreference(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerNotificationPreferenceRequestDto request) {

        CustomerNotificationPreferenceResponseDto response = notificationService.updateNotification(customerId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch notification preferences for a customer.
     *
     * @param customerId Customer UUID
     * @return ResponseEntity with customer's notification preferences
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{customerId}/notification-preferences")
    @Operation(summary = "Get Notification Preferences", description = "Fetches notification preferences for a customer")
    public ResponseEntity<CustomerNotificationPreferenceResponseDto> getNotificationPreferences(
            @PathVariable UUID customerId) {

        CustomerNotificationPreferenceResponseDto response = notificationService.getNotificationPreferences(customerId);
        return ResponseEntity.ok(response);
    }

    /**
     * Soft deleting Notification Preference Settings for a customer
     * @param customerId
     * @return
     */
    @DeleteMapping("/{customerId}/notification-preferences")
    public ResponseEntity<CustomerNotificationPreferenceResponseDto> deleteNotification(
            @PathVariable UUID customerId) {

        CustomerNotificationPreferenceResponseDto response = notificationService.deleteNotificationPreference(customerId);
        return ResponseEntity.ok(response);


    }
}
