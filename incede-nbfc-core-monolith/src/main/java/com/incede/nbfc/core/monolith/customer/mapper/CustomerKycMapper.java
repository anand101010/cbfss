package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.customer.dto.InitialCustomerProfileDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerKycMapper {

    private final CustomerRepository customerRepository;

//    public CustomerKyc toKycEntity(InitialOnboardingRequestDto request){
//
//
//
//        return customerKyc;
//    }

//    public Customer toCustomerEntity(InitialOnboardingRequestDto request){
//
//
//
//        return customer;
//    }

    public CustomerKycResponseDto toResponseDto(Customer savedCustomer, CustomerKyc savedCustomerKyc){

        CustomerKycResponseDto response = new CustomerKycResponseDto();

        response.setCustomerId(savedCustomer.getCustomerId());
        response.setCustomerCode(savedCustomer.getCustomerCode());
        response.setStatus(savedCustomer.getOnboardingStatus());
        response.setIdentity(savedCustomer.getIdentity());
        response.setIdType(savedCustomerKyc.getIdType());
        response.setIdNumber(savedCustomerKyc.getIdNumber());
        response.setPlaceOfIssue(savedCustomerKyc.getPlaceOfIssue());
        response.setIssuingAuthority(savedCustomerKyc.getIssuingAuthority());
        response.setValidFrom(savedCustomerKyc.getValidFrom());
        response.setValidTo(savedCustomerKyc.getValidTo());
        response.setDocumentRefId(savedCustomerKyc.getDocumentRefId());

        InitialCustomerProfileDto initialCustomerProfileDto = new InitialCustomerProfileDto();
        initialCustomerProfileDto.setTenantId(savedCustomer.getTenantId());
        initialCustomerProfileDto.setFirstName(savedCustomer.getFirstName());
        initialCustomerProfileDto.setLastName(savedCustomer.getLastName());
        initialCustomerProfileDto.setDob(savedCustomer.getDob());


        response.setInitialProfile(initialCustomerProfileDto);

        return response;
    }

    public String generateCustomerCode(String branchCode, String customerType) {
        Customer lastCustomer = customerRepository.findTopByOrderByCustomerIdDesc();

        String customerTypeShort = customerType.length() >= 3 ? customerType.substring(0, 3).toUpperCase() : customerType.toUpperCase();

        int incrementalId = 1;

        if (lastCustomer != null) {
            String lastCode = lastCustomer.getCustomerCode();
            String serialPart = lastCode.substring(lastCode.lastIndexOf("-") + 1);

            int lastIncrementalId = Integer.parseInt(serialPart);
            incrementalId = lastIncrementalId + 1;

        }

        String serialFormatted = "0" + incrementalId;

        return branchCode + "-" + customerTypeShort + "-" + serialFormatted;
    }

}
