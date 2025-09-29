package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalReferenceNameResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAdditionalReferenceNameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerAdditionalReferenceNameController {


    private final CustomerAdditionalReferenceNameService customerAdditionalReferenceNameService;

    /**
     * get additional reference name
     * @param tenantIdentity
     * @return
     */
    @GetMapping("/{tenantIdentity}/getAdditional-ref-name")
    public ResponseEntity<List<CustomerAdditionalReferenceNameResponseDto>> getReferenceNameIdentity(
            @PathVariable UUID tenantIdentity
    ){
        List<CustomerAdditionalReferenceNameResponseDto> customerAdditionalReferenceNameDto = customerAdditionalReferenceNameService.getReferenceName(tenantIdentity);
        return ResponseEntity.ok(customerAdditionalReferenceNameDto);
    }

}
