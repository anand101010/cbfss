package com.incede.nbfc.notification.service;

import com.incede.nbfc.notification.client.OtpNotificationFeignService;
import com.incede.nbfc.notification.common.CommonConstants;
import com.incede.nbfc.notification.config.UserConfiguration;
import com.incede.nbfc.notification.domain.TemplateCatalog;
import com.incede.nbfc.notification.domain.TemplateContents;
import com.incede.nbfc.notification.exception.BadGatewayException;
import com.incede.nbfc.notification.exception.ErrorCodes;
import com.incede.nbfc.notification.exception.ResourceNotFoundException;
import com.incede.nbfc.notification.repository.ChannelRepository;
import com.incede.nbfc.notification.repository.TemplateCatalogRepository;
import com.incede.nbfc.notification.repository.TemplateContentsRepository;
import com.incede.nbfc.notification.dto.RequestOtpDto;
import com.incede.nbfc.notification.dto.response.RequestOtpResponseDto;
import com.incede.nbfc.notification.crypto.OtpCrypto;
import com.incede.nbfc.notification.domain.OtpRequest;
import com.incede.nbfc.notification.repository.OtpRequestRepository;
import com.incede.nbfc.notification.util.RequestUrlHolder;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OtpRequestService {

	private final OtpRequestRepository repository;
	private final OtpCrypto crypto;
    private final OtpNotificationFeignService otpNotificationFeignService;
    private final TemplateContentsRepository templateContentsRepository;
    private final TemplateCatalogRepository templateCatalogRepository;

    @Value("${otp.default.length:6}")
	private int defaultLength;

	@Value("${otp.default.ttlSeconds:300}")
	private int defaultTtl;

	@Value("${otp.hash.secretKey}")
	private String hashSecretKey;

    @Value("${otp-vendor.api.pusheid}")
    private String pushid;

    @Value("${otp-vendor.api.enterpriseid}")
    private String enterpriseid;

    @Value("${otp-vendor.api.subEnterpriseid}")
    private String subEnterpriseid;

    @Value("${otp-vendor.api.pushepwd}")
    private String pushepwd;

    @Value("${otp-vendor.api.sender}")
    private String sender;


    public OtpRequestService(OtpRequestRepository repository, OtpCrypto crypto, OtpNotificationFeignService otpNotificationFeignService, TemplateContentsRepository templateContentsRepository,TemplateCatalogRepository templateCatalogRepository) {
		this.repository = repository;
		this.crypto = crypto;
		this.otpNotificationFeignService = otpNotificationFeignService;
		this.templateContentsRepository = templateContentsRepository;
		this.templateCatalogRepository = templateCatalogRepository;

	}

    /**
     *
     * @param idempotencyKey
     * @param requestOtpDto
     * @return RequestOtpResponseDto
     * here the  idempotencyKey and the requestOtpDto will carry out the data  initially it goes through the normalised methid to get an error free mssid
     * after that slot key generated then we write a method secureDigits create Otp and after that it will do hashing and salt for verify ,then it will pass to the payload for Otp triggering
     */
	@Transactional
	public RequestOtpResponseDto requestOtp(String idempotencyKey, RequestOtpDto requestOtpDto)
    {
        log.info("Otp service request received ");
        OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.UTC);

           TemplateContents templateContents = templateContentsRepository.findByIdentityAndTenantIdAndIsActiveTrueAndIsDeleteFalse(requestOtpDto.templateContentIdentity(),requestOtpDto.tenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Template content not found for ID: " + requestOtpDto.templateContentIdentity()));

        TemplateCatalog templateCatalog = templateCatalogRepository.findByIdentityAndTenantIdAndIsActiveTrueAndIsDeleteFalse(requestOtpDto.templateCatalogIdentity(),requestOtpDto.tenantId())
                .orElseThrow(() -> new ResourceNotFoundException("template Catalog  not found for ID: " + requestOtpDto.templateContentIdentity()));


        String normalizedTarget = crypto.normalizeTarget(templateContents.getChannel().getName(), requestOtpDto.target());
        String slotKey = crypto.slotKey(requestOtpDto.tenantId(), requestOtpDto.branchCode(), templateContents.getTemplateContentId().toString(), normalizedTarget);
        if (idempotencyKey != null && !idempotencyKey.isBlank())
        {
            Optional<OtpRequest> dataExisting = repository.findFirstByTenantIdAndBranchCodeAndIdempotencyKey(requestOtpDto.tenantId(), requestOtpDto.branchCode(), idempotencyKey);
            if (dataExisting.isPresent())
            {
                OtpRequest otpRequest = dataExisting.get();
                return new RequestOtpResponseDto(otpRequest.getIdentity(), otpRequest.getExpiresAt(), otpRequest.getStatus());
            }
        }
        int length = requestOtpDto.length() != null ? requestOtpDto.length() : defaultLength;
        int ttl = requestOtpDto.ttlSeconds() != null ? requestOtpDto.ttlSeconds() : defaultTtl;
        String code = crypto.secureDigits(length);
        byte[] salt = crypto.randomSalt(CommonConstants.INITIAL_RANDOM_SALT_LENGTH);
        String hash = crypto.hashOtpCode(hashSecretKey, code, salt);
        OffsetDateTime expiresAt = offsetDateTime.plusSeconds(ttl);
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.setIdentity(UUID.randomUUID());
        otpRequest.setTenantId(requestOtpDto.tenantId());
        otpRequest.setBranchCode(requestOtpDto.branchCode());
        otpRequest.setCustomerIdentity(requestOtpDto.customerIdentity());
        otpRequest.setTemplateCatalogId(templateCatalog.getTemplateCatalogId());
        otpRequest.setTemplateContentId(templateContents.getTemplateContentId());
        otpRequest.setMsisdn(normalizedTarget);
        otpRequest.setActiveSlotKey(slotKey);
        otpRequest.setIdempotencyKey(idempotencyKey);
        otpRequest.setOtpHash(hash);
        otpRequest.setOtpSalt(salt);
        otpRequest.setOtpLength((short) length);
        otpRequest.setExpiresAt(expiresAt);
        otpRequest.setMaxAttempts(CommonConstants.DEFAULT_MAX_ATTEMPTS);
        otpRequest.setAttemptCount(CommonConstants.INITIAL_ATTEMPT_COUNT);
        otpRequest.setStatus(CommonConstants.CREATED);
        otpRequest.setCreatedBy(UserConfiguration.getUser());
        otpRequest.setCreatedAt(requestOtpDto.createdAt() != null ? requestOtpDto.createdAt() : offsetDateTime);
        otpRequest.setUpdatedBy(null);
        otpRequest.setUpdatedAt(null);
        OtpRequest otpRequestResponse;
        try
        {
            log.info("save triggered");
            otpRequestResponse = repository.saveAndFlush(otpRequest);
        }
        catch (DataIntegrityViolationException exception)
        {
            throw exception;
        }
        catch (Exception ex)
        {
            log.error("Exception occurred while saving OTP: {}", ex.getMessage(), ex);
            throw ex;
        }

        String messagebody = getTemplateBody(requestOtpDto.tenantId(),templateCatalog.getTemplateCatalogId(), templateContents.getPurpose().getPurposeId(), templateContents.getChannel().getChannelId());
        String providerResponseId = otpNotificationFeignService.generateOtpNotification(enterpriseid, subEnterpriseid, pushid, pushepwd, normalizedTarget, sender, crypto.appendOtpMessage(messagebody, code));
        if (providerResponseId == null || providerResponseId.isEmpty())
        {

            throw new BadGatewayException("Unable to Communicate with Otp service", ErrorCodes.BAD_GATEWAY);

        }

        OtpRequest otpSentRequest = repository.findByIdentityForUpdate(otpRequestResponse.getIdentity())
                .orElseThrow(() -> new IllegalArgumentException(CommonConstants.OTPREQUESTNOTFOUND));
        otpSentRequest.setStatus(CommonConstants.SENT);
        otpSentRequest.setProviderResponseId(providerResponseId);
        otpSentRequest.setProviderRequestJson(RequestUrlHolder.getUrl().toString());
        RequestUrlHolder.clear();
        try
        {
            log.info("Response save method triggered");
            repository.save(otpSentRequest);
        }
        catch (DataIntegrityViolationException exception)
        {
            throw exception;
        }
        return new RequestOtpResponseDto(otpSentRequest.getIdentity(), otpSentRequest.getExpiresAt(), otpSentRequest.getStatus());

    }
    /**
     *
     * @param tenantId
     * @param channel
     * @param
     * @return  the template of the msg
     *
     */
    public String getTemplateBody(Integer tenantId,Integer templateId,Integer purpose, Integer channel) {
        return templateContentsRepository.findBodyByTenantIdAndTemplateCatalogIdAndPurposeAndChannel(tenantId,templateId,purpose,channel)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found for given parameters"));
    }
} 