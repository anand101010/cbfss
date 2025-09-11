package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerBankAccount;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.enums.PdStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for converting between CustomerBankAccount entity and DTOs.
 *
 * Author: Incede NBFC Development Team
 * Version: 1.0.0
 */
@Component
public class CustomerBankAccountMapper {

    /**
     * Convert a request DTO to a CustomerBankAccount entity.
     *
     * @param dto The request DTO
     * @return CustomerBankAccount entity
     */

    public CustomerBankAccount toEntity (CustomerBankAccountRequestDto dto){
        CustomerBankAccount customerBankAccount = new CustomerBankAccount();
        customerBankAccount.setBankName(dto.getBankName());
        customerBankAccount.setBranchName(dto.getBranchName());
        customerBankAccount.setIfscCode(dto.getIfscCode());
        customerBankAccount.setAccountNumber(dto.getAccountNumber());
        customerBankAccount.setUpiId(dto.getUpiId());
        customerBankAccount.setAccountType(dto.getAccountType());
        customerBankAccount.setAccountStatus(dto.getAccountStatus() != null ? String.valueOf(dto.getAccountStatus()) : null);
        customerBankAccount.setAccountHolderName(dto.getAccountHolderName());
        customerBankAccount.setBranchName(dto.getBranchName());

        customerBankAccount.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);
        customerBankAccount.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        customerBankAccount.setPdStatus(dto.getPdStatus());
        customerBankAccount.setPdTxnId(dto.getPdTxnId());
        customerBankAccount.setCreatedBy(dto.getCreatedBy());
        customerBankAccount.setUpiVerified(dto.getUpiVerified() != null ? dto.getUpiVerified() : false);

        return customerBankAccount;

    }

    /**
     * Update an existing CustomerBankAccount entity with values from the DTO.
     *
     *
     * @param dto DTO with updated values
     */

    public void updateEntityFromDto(CustomerBankAccount customerBankAccount, CustomerBankAccountRequestDto dto) {
        customerBankAccount.setBankName(dto.getBankName());
        customerBankAccount.setIfscCode(dto.getIfscCode());
        customerBankAccount.setBranchName(dto.getBranchName());
        customerBankAccount.setAccountNumber(dto.getAccountNumber());
        customerBankAccount.setUpiId(dto.getUpiId());
        customerBankAccount.setAccountType(dto.getAccountType());
        customerBankAccount.setAccountStatus(dto.getAccountStatus());
        customerBankAccount.setAccountHolderName(dto.getAccountHolderName());
        customerBankAccount.setBranchName(dto.getBranchName());
        customerBankAccount.setBankProofDocumentRefId(dto.getBankProofDocumentRefId());

        customerBankAccount.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);
        customerBankAccount.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        customerBankAccount.setUpiVerified(dto.getUpiVerified() != null ? dto.getUpiVerified() : false);
    }

    public CustomerBankAccountResponseDto.BankAccount toAccountDetail(CustomerBankAccount account) {
        return CustomerBankAccountResponseDto.BankAccount.builder()
                .bankName(account.getBankName())
                .branchName(account.getBranchName())
                .ifscCode(account.getIfscCode())
                .accountNumber(account.getAccountNumber())
                .maskedAccountNumber(maskAccountNumber(account.getAccountNumber()))
                .upiId(account.getUpiId())
                .accountType(account.getAccountType())
                .accountStatus(account.getAccountStatus())
                .accountHolderName(account.getAccountHolderName())
                .isPrimary(account.getIsPrimary())
                .upiVerified(account.getUpiVerified())
                .pdStatus(String.valueOf(account.getPdStatus()))
                .isActive(account.getIsActive())
                .build();
    }
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return "****";
        }
        return "******" + accountNumber.substring(accountNumber.length() - 4);
    }

    public CustomerBankAccountResponseDto toResponse(Customer customer, String status, List<CustomerBankAccountResponseDto.BankAccount > bankAccount ) {
        return CustomerBankAccountResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(status)
                .bankAccounts(bankAccount)
                .build();
    }
}
