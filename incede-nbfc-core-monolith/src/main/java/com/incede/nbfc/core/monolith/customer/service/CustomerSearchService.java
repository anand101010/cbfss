package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerSearchService {

    private final CustomerRepository customerRepository;

    public List<CustomerSearchResponseDto> searchCustomers(CustomerSearchRequestDto searchRequest) {
        log.info("Searching customers with criteria: {}", searchRequest);

        List<Customer> customers = customerRepository.searchCustomers(
                searchRequest.getBranchCode(),
                searchRequest.getBranchId(),
                searchRequest.getMobileNumber(),
                searchRequest.getEmailId(),
                searchRequest.getPanCard(),
                searchRequest.getAadhaarNumber(),
                searchRequest.getVoterId(),
                searchRequest.getPassportNumber(),
                searchRequest.getCustomerName()
        );

        log.info("Found {} customers matching search criteria", customers.size());

        return customers.stream()
                .map(this::mapToSearchResponseDto)
                .collect(Collectors.toList());
    }

    private CustomerSearchResponseDto mapToSearchResponseDto(Customer customer) {
        return CustomerSearchResponseDto.builder()
                .customerIdentity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .middleName(customer.getMiddleName())
                .lastName(customer.getLastName())
                .displayName(customer.getDisplayName())
                .mobile(customer.getMobileNumber())
                .branchCode(customer.getBranchId() != null ? customer.getBranchId().getBranchCode() : null)
                .build();
    }
}





