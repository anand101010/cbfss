package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomerSearchService {
    private final CustomerRepository customerRepository;
    private final LeadRepository leadRepository;

    @Transactional
    public List<CustomerSearchResponseDto> searchCustomers(CustomerSearchRequestDto searchRequest) {
        log.info("Searching customers with criteria: {}", searchRequest);

        List<Customer> customers;

            customers = customerRepository.searchCustomersFlexible(
                    searchRequest.getBranchCode(),
                    searchRequest.getBranchId(),
                    searchRequest.getMobileNumber() != null ? searchRequest.getMobileNumber().toString() : null,
                    searchRequest.getEmailId(),
                    searchRequest.getPanCard(),
                    searchRequest.getAadhaarNumber(),
                    searchRequest.getVoterId(),
                    searchRequest.getPassportNumber(),
                    searchRequest.getCustomerName()
            );
            log.info("Found {} customers with branch criteria", customers.size());


        if (customers.isEmpty()) {
            log.info("No customers found, searching in leads table");

            String mobile = searchRequest.getMobileNumber() != null ?
                    searchRequest.getMobileNumber().toString() : null;
            String email = searchRequest.getEmailId() != null && !searchRequest.getEmailId().trim().isEmpty() ?
                    searchRequest.getEmailId().trim() : null;
            String name = searchRequest.getCustomerName() != null && !searchRequest.getCustomerName().trim().isEmpty() ?
                    searchRequest.getCustomerName().trim() : null;

            List<Lead> leads = leadRepository.searchLeads(mobile, email, name);
            log.info("Found {} leads matching search criteria", leads.size());

            return leads.stream()
                    .map(this::mapLeadToSearchResponseDto)
                    .collect(Collectors.toList());
        }

        return customers.stream()
                .map(this::mapToSearchResponseDto)
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