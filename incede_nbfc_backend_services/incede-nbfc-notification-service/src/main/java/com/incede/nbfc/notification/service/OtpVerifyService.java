package com.incede.nbfc.notification.service;

import com.incede.nbfc.notification.common.CommonConstants;
import com.incede.nbfc.notification.config.UserConfiguration;
import com.incede.nbfc.notification.dto.VerifyOtpDto;
import com.incede.nbfc.notification.dto.response.VerifyOtpResponseDto;
import com.incede.nbfc.notification.crypto.OtpCrypto;
import com.incede.nbfc.notification.domain.OtpRequest;
import com.incede.nbfc.notification.repository.OtpRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class OtpVerifyService {

	private final OtpRequestRepository repository;
	private final OtpCrypto crypto;
	private final String hashSecretKey;

	public OtpVerifyService(OtpRequestRepository repository, OtpCrypto crypto, @Value("${otp.hash.secretKey}") String hashSecretKey)
    {
		this.repository = repository;
		this.crypto = crypto;
		this.hashSecretKey = hashSecretKey;
	}

    /**
     *
     * @param requestId the response of the send otp
     * @param verifyOtpDto  with sensituive data
     * @return  response of otp
     */
	@Transactional
	public VerifyOtpResponseDto verify(UUID requestId, VerifyOtpDto verifyOtpDto)
    {
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

		OtpRequest otpRequest = repository.findByIdentityForUpdate(requestId)
			.orElseThrow(() -> new IllegalArgumentException(CommonConstants.OTPREQUESTNOTFOUND));

		if ( CommonConstants.VERIFIED.equals(otpRequest.getStatus()) ||  CommonConstants.FAILED.equals(otpRequest.getStatus()) || CommonConstants.EXPIRED.equals(otpRequest.getStatus()) || CommonConstants.CANCELLED.equals(otpRequest.getStatus()))
        {
			return new VerifyOtpResponseDto(otpRequest.getStatus(), 0);
		}

		if (now.isAfter(otpRequest.getExpiresAt()))
        {

			otpRequest.setStatus( CommonConstants.EXPIRED);
			otpRequest.setUpdatedAt(verifyOtpDto.updatedAt() != null ? verifyOtpDto.updatedAt() : now);
            otpRequest.setUpdatedBy(UserConfiguration.getUser());
			repository.save(otpRequest);
			return new VerifyOtpResponseDto( CommonConstants.EXPIRED, 0);
		}


		if (otpRequest.getAttemptCount() >= otpRequest.getMaxAttempts())
        {
            otpRequest.setStatus(CommonConstants.FAILED);
            otpRequest.setUpdatedAt(verifyOtpDto.updatedAt() != null ? verifyOtpDto.updatedAt() : now);
            otpRequest.setUpdatedBy(UserConfiguration.getUser());
			repository.save(otpRequest);
			return new VerifyOtpResponseDto( CommonConstants.LOCKED, 0);
		}

		boolean verified = crypto.verifyOtpCode(otpRequest.getOtpHash(), hashSecretKey, verifyOtpDto.code(), otpRequest.getOtpSalt());
		if (verified)
         {
            otpRequest.setStatus(CommonConstants.VERIFIED);
            otpRequest.setUpdatedAt(verifyOtpDto.updatedAt() != null ? verifyOtpDto.updatedAt() : now);
            otpRequest.setUpdatedBy(UserConfiguration.getUser());
            otpRequest.setAttemptCount((short) (otpRequest.getAttemptCount() + 1));
			repository.save(otpRequest);
			int remaining = otpRequest.getMaxAttempts() - otpRequest.getAttemptCount();
			return new VerifyOtpResponseDto( CommonConstants.VERIFIED, remaining);
		}
        else
        {
			short newAttempts = (short) (otpRequest.getAttemptCount() + 1);
            otpRequest.setAttemptCount(newAttempts);
            otpRequest.setUpdatedAt(verifyOtpDto.updatedAt() != null ? verifyOtpDto.updatedAt() : now);
            otpRequest.setUpdatedBy(UserConfiguration.getUser());
			if (newAttempts >= otpRequest.getMaxAttempts())
            {
                otpRequest.setStatus(CommonConstants.FAILED);
				repository.save(otpRequest);
				return new VerifyOtpResponseDto( CommonConstants.LOCKED, 0);
			}
            else
            {
				repository.save(otpRequest);
				int remaining = otpRequest.getMaxAttempts() - newAttempts;
				return new VerifyOtpResponseDto( CommonConstants.INVALID, remaining);
			}
		}
	}
} 