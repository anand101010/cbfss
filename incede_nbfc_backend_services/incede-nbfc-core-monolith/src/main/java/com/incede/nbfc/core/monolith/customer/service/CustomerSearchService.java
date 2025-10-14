package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.service.VaultService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomerSearchService {

    private final CustomerRepository customerRepository;
    private final LeadRepository leadRepository;
    private final VaultService vaultService;

    @Transactional
    public List<CustomerSearchResponseDto> searchCustomers(CustomerSearchRequestDto searchRequest) {
        Objects.requireNonNull(searchRequest, "Search request must not be null");
        log.info("Searching customers with criteria: {}", searchRequest);

        String aadhaarVaultRef = null;
        if (searchRequest.getAadhaarNumber() != null && !searchRequest.getAadhaarNumber().isBlank()) {
            try {
                FinaVaultResponseDto vaultResponse =
                        vaultService.generateVaultIdAndMaskAadhaar(searchRequest.getAadhaarNumber());
                if (vaultResponse != null) {
                    aadhaarVaultRef = vaultResponse.getUidReferenceKey();
                }
            } catch (Exception e) {
                log.error("Error while masking Aadhaar / fetching vault reference", e);
            }
        }

        List<Customer> customers = customerRepository.searchCustomersFlexible(
                searchRequest.getBranchCode(),
                searchRequest.getBranchId(),
                searchRequest.getMobileNumber() != null ? searchRequest.getMobileNumber().toString() : null,
                searchRequest.getEmailId(),
                searchRequest.getPanCard(),
                aadhaarVaultRef,
                searchRequest.getVoterId(),
                searchRequest.getPassportNumber(),
                searchRequest.getCustomerName()
        );

        log.info("Found {} customers with search criteria", customers.size());

        if (!customers.isEmpty()) {
            return customers.stream()
                    .map(this::mapToSearchResponseDto)
                    .collect(Collectors.toList());
        }

        boolean hasCustomerSpecificFields =
                (aadhaarVaultRef != null)
                        || (searchRequest.getPanCard() != null && !searchRequest.getPanCard().isBlank())
                        || (searchRequest.getVoterId() != null && !searchRequest.getVoterId().isBlank())
                        || (searchRequest.getPassportNumber() != null && !searchRequest.getPassportNumber().isBlank());

        if (hasCustomerSpecificFields) {
            log.info("Customer-specific identifiers present — skipping lead search.");
            return List.of();
        }

        String mobile = searchRequest.getMobileNumber() != null ? searchRequest.getMobileNumber().toString() : null;
        String email = searchRequest.getEmailId() != null && !searchRequest.getEmailId().isBlank()
                ? searchRequest.getEmailId().trim()
                : null;
        String name = searchRequest.getCustomerName() != null && !searchRequest.getCustomerName().isBlank()
                ? searchRequest.getCustomerName().trim()
                : null;

        List<Lead> leads = leadRepository.searchLeadDetails(mobile, email, name);
        log.info("Found {} leads matching criteria", leads.size());

        return leads.stream()
                .map(this::mapLeadToSearchResponseDto)
                .collect(Collectors.toList());
    }

    private CustomerSearchResponseDto mapToSearchResponseDto(Customer customer) {
        return CustomerSearchResponseDto.builder()
                .customerIdentity(customer.getIdentity())
                .isCustomerExist(true)
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .middleName(customer.getMiddleName())
                .lastName(customer.getLastName())
                .displayName(customer.getDisplayName())
                .mobile(customer.getMobileNumber())
                .branchCode(customer.getBranchId() != null ? customer.getBranchId().getBranchCode() : null)
                .build();
    }

    private CustomerSearchResponseDto mapLeadToSearchResponseDto(Lead lead) {
        return CustomerSearchResponseDto.builder()
                .customerCode(lead.getLeadCode())
                .isLeadExist(true)
                .firstName(lead.getFullName())
                .mobile(lead.getContactNumber())
                .build();
    }
}
