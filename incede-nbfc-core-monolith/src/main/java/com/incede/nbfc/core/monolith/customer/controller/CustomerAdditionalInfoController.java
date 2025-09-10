package com.incede.nbfc.core.monolith.customer.controller;


import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAdditionalInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Additional information Api", description = "Customer Additional information APIs")
public class CustomerAdditionalInfoController {

    @Autowired
    CustomerAdditionalInfoService additionalInfoService;

    /**
     * update additional information for customer
     * @param customerIdentity
     * @param request
     * @return
     */
    @PutMapping("/{customerIdentity}/additional")
    public ResponseEntity<CustomerAdditionalInfoResponseDto> updateAdditionalInfo(
            @PathVariable UUID customerIdentity,
            @Valid @RequestBody CustomerAdditionalInfoRequestDto request
    ){

        CustomerAdditionalInfoResponseDto response = additionalInfoService.saveAdditionalInfo(customerIdentity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{customerIdentity}/additional")
    public ResponseEntity<CustomerAdditionalInfoResponseDto> getAdditionalInfo(
            @PathVariable UUID customerIdentity
    ){

        CustomerAdditionalInfoResponseDto response = additionalInfoService.getAdditionalInfo(customerIdentity);
        return ResponseEntity.ok(response);
    }

}

