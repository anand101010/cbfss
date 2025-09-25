package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAdditionalInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller to manage Additional Information of Customers.
 * Provides endpoints to create/update and fetch additional info for a given customer.
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Additional information Api", description = "Customer Additional information APIs")
public class CustomerAdditionalInfoController {

    private  final CustomerAdditionalInfoService additionalInfoService;

    /**
     * Update or create additional information for a customer.
     *
     * @param customerIdentity the unique UUID of the customer
     * @param request          the additional info details to save
     * @return ResponseEntity with created (201) status and saved additional info
     */
    @PutMapping("/{customerIdentity}/additional")
    @Operation(summary = "Create Additional Info", description = "Creates Additional info for a customer")
    public ResponseEntity<CustomerAdditionalInfoResponseDto> updateAdditionalInfo(
            @PathVariable UUID customerIdentity,
            @Valid @RequestBody CustomerAdditionalInfoRequestDto request
    ) {
        CustomerAdditionalInfoResponseDto response = additionalInfoService.saveAdditionalInfo(customerIdentity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get the additional information of a specific customer.
     *
     * @param customerIdentity the unique UUID of the customer
     * @return ResponseEntity with 200 OK status and additional info data
     */
    @GetMapping("/{customerIdentity}/additional")
    @Operation(summary = "Get Additional Info", description = "Fetches Additional info for a customer")
    public ResponseEntity<CustomerAdditionalInfoResponseDto> getAdditionalInfo(
            @PathVariable UUID customerIdentity
    ) {
        CustomerAdditionalInfoResponseDto response = additionalInfoService.getAdditionalInfo(customerIdentity);
        return ResponseEntity.ok(response);
    }
}
