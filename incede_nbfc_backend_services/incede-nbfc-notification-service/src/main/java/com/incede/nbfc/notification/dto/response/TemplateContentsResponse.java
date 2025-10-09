package com.incede.nbfc.notification.dto.response;



import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateContentsResponse
{

    private String channel;
    private String languageCode;
    private String subject;
    private String body;
    private String footer;
    private Integer maxLength;
    private String senderId;
    private String providerParams;
    private String dltPrincipalId;
    private String dltTemplateId;
    private String checksum;
    private UUID identity;

}
