package com.incede.nbfc.notification.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TemplateContentsRequestDto
{

    @NotNull(message = "Template ID is required")
    private Integer templateId;

    @NotBlank(message = "Channel is required")
    @Size(max = 16, message = "Channel cannot exceed 16 characters")
    private String channel;

    @NotBlank(message = "Language code is required")
    @Size(max = 10, message = "Language code cannot exceed 10 characters")
    private String languageCode;

    @Size(max = 255, message = "purpose cannot exceed 255 characters")
    private String purpose;

    @Size(max = 255, message = "Subject cannot exceed 255 characters")
    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    @Size(max = 255, message = "Footer cannot exceed 255 characters")
    private String footer;

    @Min(value = 1, message = "Max length must be greater than 0")
    private Integer maxLength;

    @Size(max = 20, message = "Sender ID cannot exceed 20 characters")
    private String senderId;

    private String providerParams;

    @Size(max = 50, message = "DLT Principal ID cannot exceed 50 characters")
    private String dltPrincipalId;

    @Size(max = 50, message = "DLT Template ID cannot exceed 50 characters")
    private String dltTemplateId;

    @NotNull(message = "isActive flag cannot be null")
    private Boolean isActive =true;

    @NotNull(message = "isDel flag cannot be null")
    private Boolean isDel = false;

}
