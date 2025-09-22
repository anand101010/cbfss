package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CkycRequestDto;
import com.incede.nbfc.core.monolith.customer.service.CkycService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ckyc")
public class CkycController {

    private final CkycService ckycService;

    public CkycController(CkycService ckycService) {
        this.ckycService = ckycService;
    }

    @PostMapping("/prepare-request")
    public ResponseEntity<String> prepareRequest(@RequestBody CkycRequestDto dto) {
        String signedXml = ckycService.buildSignedRequest(dto);
        return ResponseEntity.ok(signedXml);
    }
}

