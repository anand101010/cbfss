package com.incede.nbfc.core.monolith.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerPhotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/")
@RequiredArgsConstructor
public class CustomerPhotoController {

    private final CustomerPhotoService customerPhotoService;

    /**
     * Create a new photo entry for a given customer.
     *
     * @param customerUUID   the unique identity of the customer
     * @param requestJson DTO containing photo details
     * @return ResponseEntity with created photo details
     */
    @PostMapping(value = "{customerUUID}/photo", consumes = {"multipart/form-data"})
    public ResponseEntity<CustomerPhotoResponseDto> createPhoto(
            @PathVariable UUID customerUUID,
            @RequestPart("request") String requestJson,
            @RequestPart("file") MultipartFile file
    ) {
        CustomerPhotoResponseDto createdPhoto = customerPhotoService.createPhoto(customerUUID, requestJson, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPhoto);
    }




    /**
     * Retrieve all photos associated with a given customer identity.
     *
     * @param customerUUID the unique identity of the customer
     * @return ResponseEntity with customer photo details
     */
    @GetMapping("{customerUUID}/photo")
    public ResponseEntity<CustomerPhotoResponseDto> getPhoto(@PathVariable UUID customerUUID) {
        CustomerPhotoResponseDto photoResponse = customerPhotoService.getCustomerPhotos(customerUUID);
        return ResponseEntity.ok(photoResponse);
    }



}
