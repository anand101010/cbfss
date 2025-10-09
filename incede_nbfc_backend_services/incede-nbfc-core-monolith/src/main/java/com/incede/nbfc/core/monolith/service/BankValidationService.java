package com.incede.nbfc.core.monolith.service;
import com.incede.nbfc.core.monolith.client.PennydropClient;
import com.incede.nbfc.core.monolith.client.dto.BankValidationRequestDto;
import com.incede.nbfc.core.monolith.client.dto.BankValidationResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.domain.dto.BankValidateRequestDto;
import com.incede.nbfc.core.monolith.exception.FeignCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankValidationService {

    private final PennydropClient pennydropclient;

    /**
     * Validates a bank account using PennyDrop service.
     *
     * @param bankValidateRequestDto request DTO containing account number and IFSC
     * @return BankValidationResponseDto response from the external service
     */
    public BankValidationResponseDto validateBankAccount(BankValidateRequestDto bankValidateRequestDto) {
        try {
            BankValidationRequestDto validationRequest = new BankValidationRequestDto();
            String referenceId = UUID.randomUUID().toString();
            validationRequest.setReferenceId(referenceId);
            validationRequest.setPurposeMessage(CommonConstants.PURPOSE_MESSAGE);
            validationRequest.setTransferAmount(CommonConstants.PENNYDROP_AMNT);
            BankValidationRequestDto.BeneficiaryDetails beneficiaryDetails = new BankValidationRequestDto.BeneficiaryDetails();
            beneficiaryDetails.setAccountNumber(bankValidateRequestDto.getAccountNumber());
            beneficiaryDetails.setIfsc(bankValidateRequestDto.getIfsc());
            validationRequest.setBeneficiaryDetails(beneficiaryDetails);
            return pennydropclient.validateBankAccount(validationRequest);

        } catch (FeignCustomException exception)
        {
            throw exception;
        }
        catch (Exception e) {
            // Log and rethrow exception with context for debugging
            log.error("Bank account validation failed for Account:{}", e);

            throw new RuntimeException("Bank account validation failed", e);
        }
    }
}
