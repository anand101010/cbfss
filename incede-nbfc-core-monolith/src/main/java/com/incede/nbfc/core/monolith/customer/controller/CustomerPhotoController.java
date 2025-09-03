package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerPhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class CustomerPhotoController {

    private final CustomerPhotoService customerPhotoService;

    @PostMapping("/{identity}/photo")
    public ResponseEntity<CustomerPhotoResponseDto> createPhoto(
            @PathVariable UUID identity,
            @RequestBody @Valid CustomerPhotoRequestDto photoRequestDTO
    ){
        CustomerPhotoResponseDto createdPhoto = customerPhotoService.createPhoto( identity, photoRequestDTO);
        return ResponseEntity.ok(createdPhoto);
    }

      /*
    get By identity
    @param UUID customer Identity
    @return Customer Photos by CustomerIs
     */

    @GetMapping("/{identity}/photo")
    public ResponseEntity<CustomerPhotoResponseDto> getPhoto(@PathVariable UUID identity) {
        CustomerPhotoResponseDto photoResponse = customerPhotoService.getCustomerPhotos(identity);
        return ResponseEntity.ok(photoResponse);
    }



}