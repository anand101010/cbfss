package com.incede.nbfc.notification.controller;

import com.incede.nbfc.notification.dto.TemplateContentsRequestDto;
import com.incede.nbfc.notification.dto.response.TemplateContentsResponse;
import com.incede.nbfc.notification.service.TemplateContentsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/template-contents")
@Slf4j
public class TemplateContentsController {

    private final TemplateContentsService templateContentsService;

    public TemplateContentsController(TemplateContentsService templateContentsService) {
        this.templateContentsService = templateContentsService;
    }

    /**
     *
     * @param templateContentsRequest
     * @return TemplateContentsResponse
     */
    @PostMapping
    public ResponseEntity<TemplateContentsResponse> createTemplateContent(
            @Valid @RequestBody TemplateContentsRequestDto templateContentsRequest)
    {
        log.info("triggered template Content service");
        TemplateContentsResponse response = templateContentsService.createTemplateContent(templateContentsRequest);
        return ResponseEntity.ok(response);
    }
}
