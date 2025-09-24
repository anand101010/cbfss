package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerKycService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerKycController {

    private final CustomerKycService customerKycService;

    /**
     * create a initial customer with minimal information and save the document to DMS
     * @param requestJson
     * @param file
     * @return
     */
    @PostMapping(value = "/addKyc", consumes = {"multipart/form-data"})
    public ResponseEntity<CustomerKycResponseDto> createInitialCustomer(
            @RequestPart("request") String requestJson,
            @RequestPart("file") MultipartFile file
    ){
        CustomerKycResponseDto customerKycResponseDto = customerKycService.createInitialCustomer(requestJson, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerKycResponseDto);
    }



    @PutMapping("{customerIdentity}/addKyc")
    public ResponseEntity<CustomerKycResponseDto> addKycDocument(
            @RequestPart("request") String requestJson,
            @PathVariable UUID customerIdentity,
            @RequestPart("file") MultipartFile file
    ){
        CustomerKycResponseDto customerKycResponseDto = customerKycService.addKycDocument(requestJson, file, customerIdentity);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerKycResponseDto);
    }
}
