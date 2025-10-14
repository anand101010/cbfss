
package com.incede.nbfc.core.monolith.customer.controller;


import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.Form60UploadResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerForm60Service;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor

public class CustomerForm60Controller {

    private final CustomerForm60Service form60Service;


    /**
     * Save a new Form 60 for the given customer.
     *
     * @param customerIdentity UUID of the customer for whom Form 60 is being created
     * @param request          Request body containing Form 60 details
     * @return ResponseEntity with the saved Form 60 details and HTTP status CREATED
     */

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(value = "/{customerIdentity}/form60")
    @Operation(summary = "Save Form60", description = "Save Form60 for a new customer and returns generated identity")
    public ResponseEntity<CustomerForm60ResponseDto> saveForm60(
            @PathVariable UUID customerIdentity,
            @RequestBody CustomerForm60RequestDto request) {

        CustomerForm60ResponseDto response = form60Service.saveForm60(request, customerIdentity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing Form 60 for the given customer and Form 60 ID.
     *
     * @param customerIdentity UUID of the customer whose Form 60 needs to be updated
     * @param form60Identity         ID of the Form 60 to update
     * @param request          Request body containing updated Form 60 details
     * @return ResponseEntity with the updated Form 60 details
     */

    @PreAuthorize("hasRole('STAFF')")
    @PutMapping("/{customerIdentity}/form60/{form60Identity}")
    @Operation(summary = "Update Form 60", description = "Updates Form 60 information by customer identity and Form 60 Identity")
    public ResponseEntity<CustomerForm60ResponseDto> updateForm60(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID form60Identity,
            @Valid @RequestBody CustomerForm60RequestDto request) {
        CustomerForm60ResponseDto response = form60Service.updateForm60(customerIdentity, form60Identity, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetch Form 60 information by customer identity and Form 60 ID
     *
     * @param customerIdentity UUID of the customer
     * @param form60Identity         ID of the Form 60
     * @return ResponseEntity with Form 60 information
     */
    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{customerIdentity}/form60/{form60Identity}")
    @Operation(summary = "Get Form 60", description = "Fetches Form 60 information by customer identity and Form 60 Identity")
    public ResponseEntity<CustomerForm60ResponseDto> getForm60ByIdentity(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID form60Identity) {
        CustomerForm60ResponseDto response = form60Service.getForm60ByIdentity(customerIdentity, form60Identity);
        return ResponseEntity.ok(response);
    }

    /**
     * preview Form 60 pdf information by customer identity and Form 60 Identity
     *
     * @param customerIdentity UUID of the customer
     * @param form60Identity         ID of the Form 60
     * @return form 60 pdf
     */

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{customerIdentity}/form60/{form60Identity}/preview")
    @Operation(summary = "preview Form 60", description = "Generate Form 60 from th information by customer identity and Form 60 ID")
    public ResponseEntity<byte[]> generateForm60PreviewPdf(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID form60Identity,
            @RequestParam(defaultValue = "form60-preview.pdf") String filename) {

        byte[] pdf = form60Service.generateForm60PreviewPdf(customerIdentity, form60Identity);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(filename).build().toString())
                .body(pdf);
    }

    /**
     * Download Form 60 pdf information by customer identity and Form 60 Identity
     *
     * @param customerIdentity UUID of the customer
     * @param form60Identity         ID of the Form 60
     * @return form 60 pdf
     */

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/{customerIdentity}/form60/{form60Identity}/download")
    @Operation(summary = "download Form 60", description = "Generate Form 60 from th information by customer identity and Form 60 Identity")
    public ResponseEntity<byte[]> generateForm60DownloadPdf(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID form60Identity,
            @RequestParam(defaultValue = "form60-preview.pdf") String filename) {

        byte[] pdf = form60Service.generateForm60PreviewPdf(customerIdentity, form60Identity);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(filename)
                                .build()
                                .toString())
                .body(pdf);
    }

    /**
     * Upload signed Form 60
     *
     * @param customerIdentity UUID of the customer
     * @param form60Identity         UUID of the Form 60
     * @return response entity with upload details
     */

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping(
            value = "/{customerIdentity}/form60/{form60Identity}/upload"

    )
    @Operation(summary = "Upload Signed Form 60 PDF",
            description = "Upload the signed PDF of Form 60 for a given customer and Form 60 ID")
    public ResponseEntity<Form60UploadResponseDto> uploadSignedForm60(
            @PathVariable UUID customerIdentity,
            @PathVariable UUID form60Identity) {

        Form60UploadResponseDto response = form60Service.uploadSignedForm60(customerIdentity, form60Identity);
        return ResponseEntity.ok(response);
    }

}


