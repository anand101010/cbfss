package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerSearchController {

    private final CustomerSearchService customerSearchService;

    /**
     * POST endpoint to search customers based on criteria.
     *
     * Example:
     * POST /api/customers/search
     * {
     *   "branchCode": "BR001",
     *   "mobileNumber": "9876543210",
     *   "customerName": "John"
     * }
     */
    @PostMapping("/search")
    public ResponseEntity<List<CustomerSearchResponseDto>> searchCustomers(
            @RequestBody CustomerSearchRequestDto searchRequest) {

        log.info("Received customer search request: {}", searchRequest);

        List<CustomerSearchResponseDto> results = customerSearchService.searchCustomers(searchRequest);

        if (results.isEmpty()) {
            log.info("No customers found for criteria: {}", searchRequest);
            return ResponseEntity.noContent().build();
        }

        log.info("Returning {} customers", results.size());
        return ResponseEntity.ok(results);
    }
}
