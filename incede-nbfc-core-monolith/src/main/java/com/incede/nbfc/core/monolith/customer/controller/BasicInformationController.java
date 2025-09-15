package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import com.incede.nbfc.core.monolith.customer.service.BasicInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Basic information Api", description = "Customer Basic information APIs")
public class BasicInformationController {

    private final BasicInformationService customerOnboardingService;

    /**
     * Create basic information for a new customer.
     *
     * @param request DTO containing customer basic info
     * @return ResponseEntity with created customer's basic info and generated identity
     */
    @PostMapping("/basic")
    @Operation(summary = "Create Basic Info", description = "Creates basic info for a new customer and returns generated identity")
    public ResponseEntity<BasicInformationResponseDto> createBasicInfo(
            @Valid @RequestBody BasicInformationRequestDto request) {

        BasicInformationResponseDto response = customerOnboardingService.saveBasicInformation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update basic information for an existing customer.
     *
     * @param customerUUID    Customer UUID
     * @param request DTO containing updated basic info
     * @return ResponseEntity with updated customer's basic info
     */
    @PutMapping("/{customerUUID}/basic")
    @Operation(summary = "Update Basic Info", description = "Updates basic info for an existing customer using UUID")
    public ResponseEntity<BasicInformationResponseDto> updateBasicInfo(
            @PathVariable UUID customerUUID,
            @Valid @RequestBody BasicInformationRequestDto request) {
        BasicInformationResponseDto response = customerOnboardingService.updateBasicInformation(customerUUID, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch basic information of a customer by UUID.
     *
     * @param customerUUID Customer UUID
     * @return ResponseEntity with customer's basic info
     */
    @GetMapping("/basic/{customerUUID}")
    @Operation(summary = "Get Basic Info", description = "Fetches basic info of a customer by UUID")
    public ResponseEntity<BasicInformationResponseDto> getBasicInfoByUuid(@PathVariable UUID customerUUID) {
        BasicInformationResponseDto response = customerOnboardingService.getBasicInformationByUuid(customerUUID);
        return ResponseEntity.ok(response);
    }
}
