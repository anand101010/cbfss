package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerKycService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerKycController {

    private final CustomerKycService customerKycService;

    @PostMapping(value = "/initial", consumes = {"multipart/form-data"})
    public ResponseEntity<CustomerKycResponseDto> createInitialCustomer(
            @RequestPart("request") String requestJson,
            @RequestPart("file") MultipartFile file
    ){
        CustomerKycResponseDto customerKycResponseDto = customerKycService.createInitialCustomer(requestJson, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerKycResponseDto);
    }
}
