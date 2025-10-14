package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerKycService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerKycController {

    private final CustomerKycService customerKycService;

    /**
     * create an initial customer with minimal information and save the document to DMS
     * @return
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(value = "/addKyc")
    public ResponseEntity<CustomerKycResponseDto> createInitialCustomer(
            @RequestBody CustomerKycRequestDto customerKycRequestDto
    ){
        CustomerKycResponseDto customerKycResponseDto = customerKycService.createInitialCustomer(customerKycRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerKycResponseDto);
    }



    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("{customerIdentity}/addKyc")
    public ResponseEntity<CustomerKycResponseDto> addKycDocument(
            @RequestBody CustomerKycRequestDto customerKycRequestDto,
            @PathVariable UUID customerIdentity
    ){
        CustomerKycResponseDto customerKycResponseDto = customerKycService.addKycDocument(customerKycRequestDto, customerIdentity);
        return ResponseEntity.status(HttpStatus.OK).body(customerKycResponseDto);
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{customerIdentity}/getKyc")
    public ResponseEntity<CustomerKycResponseDto> getKycDocuments(
            @PathVariable UUID customerIdentity
    ) {
        CustomerKycResponseDto customerKycResponseDtos = customerKycService.getKycDocuments(customerIdentity);
        return ResponseEntity.status(HttpStatus.OK).body(customerKycResponseDtos);
    }

}
