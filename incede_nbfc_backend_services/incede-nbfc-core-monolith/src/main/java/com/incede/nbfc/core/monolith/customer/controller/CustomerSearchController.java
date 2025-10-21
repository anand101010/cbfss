package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CanvasserResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Customer Search", description = "APIs for searching customers with various criteria")
public class CustomerSearchController {

    private final CustomerSearchService customerSearchService;

    /**
     * GET endpoint to search customers based on multiple criteria.
     *
     * Example:
     * GET /api/v1/customers/search?branchCode=BR001&mobileNumber=9876543210&customerName=John&emailId=john@example.com
     */
    @Operation(summary = "Search customers", description = "Search customers using multiple search criteria")
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/search")
    public ResponseEntity<List<CustomerSearchResponseDto>> searchCustomers(
            @Parameter(description = "Branch code") @RequestParam (required = false)String branchCode,
            @Parameter(description = "Branch ID") @RequestParam(required = false) Integer branchId,
            @Parameter(description = "Mobile number") @RequestParam(required = false) String mobileNumber,
            @Parameter(description = "Email address") @RequestParam(required = false) String emailId,
            @Parameter(description = "PAN card number") @RequestParam(required = false) String panCard,
            @Parameter(description = "Aadhaar number") @RequestParam(required = false) String aadhaarNumber,
            @Parameter(description = "Voter ID") @RequestParam(required = false) String voterId,
            @Parameter(description = "Passport number") @RequestParam(required = false) String passportNumber,
            @Parameter(description = "Customer name") @RequestParam(required = false) String customerName) {

        CustomerSearchRequestDto searchRequest = CustomerSearchRequestDto.builder()
                .branchCode(branchCode)
                .branchId(branchId)
                .mobileNumber(mobileNumber)
                .emailId(emailId)
                .panCard(panCard)
                .aadhaarNumber(aadhaarNumber)
                .voterId(voterId)
                .passportNumber(passportNumber)
                .customerName(customerName)
                .build();

        log.info("Received customer search request with parameters: {}", searchRequest);

        List<CustomerSearchResponseDto> results = customerSearchService.searchCustomers(searchRequest);

        if (results.isEmpty()) {
            log.info("No customers found for criteria: {}", searchRequest);
            return ResponseEntity.noContent().build();
        }

        log.info("Returning {} customers", results.size());
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get Canvasser", description = "Get Canvasser by Canvassed Type ID")
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{canvassedTypeId}/{canvasserName}")
    public ResponseEntity<List<CanvasserResponseDto>> fetchCanvasserId(
            @PathVariable UUID canvassedTypeId,
            @PathVariable String canvasserName
    ){
        List<CanvasserResponseDto> result = customerSearchService.fetchCanvasserId(canvassedTypeId,canvasserName);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get Canvasser name", description = "Get Canvasser by  Identity ")
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{canvassedTypeId}/{canvasserIdentity}")
    public ResponseEntity<List<CanvasserResponseDto>> fetchCanvasserName(
            @PathVariable UUID canvassedTypeId,
            @PathVariable UUID canvasserIdentity
    ){
        List<CanvasserResponseDto> result = customerSearchService.fetchCanvasserName(canvassedTypeId,canvasserIdentity);
        return ResponseEntity.ok(result);
    }


}