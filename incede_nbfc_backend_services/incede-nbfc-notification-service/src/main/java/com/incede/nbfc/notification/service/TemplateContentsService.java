package com.incede.nbfc.notification.service;
import com.incede.nbfc.notification.config.UserConfiguration;
import com.incede.nbfc.notification.crypto.OtpCrypto;
import com.incede.nbfc.notification.domain.TemplateCatalog;
import com.incede.nbfc.notification.dto.TemplateContentsRequestDto;
import com.incede.nbfc.notification.dto.response.TemplateContentsResponse;
import com.incede.nbfc.notification.exception.ResourceNotFoundException;
import com.incede.nbfc.notification.repository.TemplateCatalogRepository;
import com.incede.nbfc.notification.repository.TemplateContentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
public class TemplateContentsService {

    private final TemplateContentsRepository contentsRepository;
    private final TemplateCatalogRepository catalogRepository;

    public TemplateContentsService(TemplateContentsRepository contentsRepository,
                                   TemplateCatalogRepository catalogRepository, OtpCrypto otpCrypto)
    {
        this.contentsRepository = contentsRepository;
        this.catalogRepository = catalogRepository;
    }

    /**
     *
     * @param request that will create the Template Contents Request
     * @return TemplateContentsResponse
     */
    @Transactional
    public TemplateContentsResponse createTemplateContent(TemplateContentsRequestDto request)
    {
        log.info("triggered template Content service Method");
        TemplateCatalog templateCatalog = catalogRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid templateId: " + request.getTemplateId()));

        String providerJson = request.getProviderParams() != null ? request.getProviderParams() : "{}";
        UUID identity = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        Integer currentUser = UserConfiguration.getUser();
        try
        {
            contentsRepository.saveNative(
                    templateCatalog.getTemplateCatalogId(),
                    request.getChannel(),
                    request.getLanguageCode(),
                    request.getSubject(),
                    request.getBody(),
                    request.getFooter(),
                    request.getMaxLength(),
                    request.getSenderId(),
                    providerJson,
                    request.getDltPrincipalId(),
                    request.getDltTemplateId(),
                    null,
                    request.getIsActive() != null ? request.getIsActive() : true,
                    currentUser,
                    now,
                    null,
                    now,
                    request.getIsDel() != null ? request.getIsDel() : false,
                    identity
            );
            log.info("triggered template Content save Method");
        } catch (DataIntegrityViolationException exception)
        {
            log.error("Error in triggering template Content service Method:{}",exception);
            throw exception;
        }
        TemplateContentsResponse templateContentsResponse = new TemplateContentsResponse();
        templateContentsResponse.setChannel(request.getChannel());
        templateContentsResponse.setLanguageCode(request.getLanguageCode());
        templateContentsResponse.setSubject(request.getSubject());
        templateContentsResponse.setBody(request.getBody());
        templateContentsResponse.setFooter(request.getFooter());
        templateContentsResponse.setMaxLength(request.getMaxLength());
        templateContentsResponse.setSenderId(request.getSenderId());
        templateContentsResponse.setProviderParams(providerJson);
        templateContentsResponse.setDltPrincipalId(request.getDltPrincipalId());
        templateContentsResponse.setDltTemplateId(request.getDltTemplateId());
        templateContentsResponse.setChecksum(null);
        templateContentsResponse.setIdentity(identity);
        return templateContentsResponse;
    }
}