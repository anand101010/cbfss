package com.incede.nbfc.notification.controller;

import com.incede.nbfc.notification.dto.RequestOtpDto;
import com.incede.nbfc.notification.dto.response.RequestOtpResponseDto;
import com.incede.nbfc.notification.dto.VerifyOtpDto;
import com.incede.nbfc.notification.dto.response.VerifyOtpResponseDto;
import com.incede.nbfc.notification.service.OtpRequestService;
import com.incede.nbfc.notification.service.OtpVerifyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/otp")
@Slf4j
public class OtpRequestController {

	private final OtpRequestService otpRequestService;
	private final OtpVerifyService otpVerifyService;

	public OtpRequestController(OtpRequestService otpRequestService,OtpVerifyService otpVerifyService)
    {
		this.otpRequestService = otpRequestService;
		this.otpVerifyService = otpVerifyService;
	}

    /**
     *
     * @param idempotencyKey  to maintain the stability avoid unwanted continious calls
     * @param requestOtpDto
     * @return RequestOtpResponseDto
     */
    @PreAuthorize("hasRole('STAFF')")
	@PostMapping("/send")
	public ResponseEntity<RequestOtpResponseDto> requestOtp(@RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey, @Valid @RequestBody RequestOtpDto requestOtpDto)
    {
        log.info("send OTP triggered");
		RequestOtpResponseDto requestOtpResponseDto = otpRequestService.requestOtp(idempotencyKey, requestOtpDto);
        return ResponseEntity.status(201).body(requestOtpResponseDto);
	}

    /**
     *
     * @param requestId  that will refer the request otp reference otp reference for validation
     * @param verifyOtpDto
     * @return VerifyOtpResponseDto
     */
    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/{requestId}/verify")
    public ResponseEntity<VerifyOtpResponseDto> verify(@PathVariable("requestId") UUID requestId,@Valid @RequestBody VerifyOtpDto verifyOtpDto)
    {
        log.info("OTP Verification triggered");
        VerifyOtpResponseDto resp = otpVerifyService.verify(requestId, verifyOtpDto);
        return ResponseEntity.ok(resp);
    }
} 