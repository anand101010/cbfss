package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerAdditionalInfoService {

    private final CustomerAdditionalInfoMapper customerAdditionalInfoMapper;

    private final CustomerRepository customerRepository;
    private final CustomerEmploymentRepository employmentRepository;
    private final CustomerReferralRepository referralRepository;
    private final CustomerPepRepository pepRepository;
    private final CustomerProfileExtraRepository profileExtraRepository;
    private final CustomerAssetRepository assetRepository;


    /**
     * Saves additional customer information (such as profile extras, referrals, assets, etc.)
     *
     * @param identity the unique identity of the customer
     * @param dto      DTO containing the additional information details
     * @return a response DTO confirming the saved information
     */
    @Transactional
    public CustomerAdditionalInfoResponseDto saveAdditionalInfo(UUID identity, CustomerAdditionalInfoRequestDto dto) {
        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE,
                        ErrorCodes.RESOURCE_NOT_FOUND));





        CustomerEmployment employment = employmentRepository.findByCustomer(customer)
                .orElseGet(CustomerEmployment::new);
        customerAdditionalInfoMapper.updateEmployment(employment, dto.getAdditional().getEmployment(), customer);
        employmentRepository.save(employment);


        CustomerReferral referral = referralRepository.findByCustomer(customer)
                .orElseGet(CustomerReferral::new);
        customerAdditionalInfoMapper.updateReferral(referral, dto.getAdditional().getReferrals(), customer);
        referralRepository.save(referral);


        CustomerPep pep = pepRepository.findByCustomer(customer)
                .orElseGet(CustomerPep::new);
        customerAdditionalInfoMapper.updatePep(pep, dto.getAdditional().getPep(), customer);
        pepRepository.save(pep);

        CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer)
                .orElseGet(CustomerProfileExtra::new);
        customerAdditionalInfoMapper.updateProfileExtra(profileExtra, dto.getAdditional().getProfileExtra(), customer);
        profileExtraRepository.save(profileExtra);

        CustomerAsset asset =
               customerAdditionalInfoMapper.mapToAsset(dto.getAdditional().getCustomerAsset(), customer);

        assetRepository.save(asset);

        Customer updatedCustomer=customerAdditionalInfoMapper.updateCustomerFromAdditionalInfo(customer, dto.getAdditional().getCustomer());

        customerRepository.save(updatedCustomer);

        return customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, pep, profileExtra, asset);


    }

    /**
     * Fetch all additional information for a given customer.
     *
     * @param customerIdentity UUID of the customer
     * @return CustomerAdditionalInfoResponseDto containing all related details
     */
    @Transactional(readOnly = true)
    public CustomerAdditionalInfoResponseDto getAdditionalInfo(UUID customerIdentity) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE,
                        ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerEmployment employment = employmentRepository.findByCustomer(customer).orElse(null);
        CustomerReferral referral = referralRepository.findByCustomer(customer).orElse(null);
        CustomerPep pep = pepRepository.findByCustomer(customer).orElse(null);
        CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer).orElse(null);
        CustomerAsset assets = assetRepository.findByCustomer(customer);

        return customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, pep, profileExtra, assets);
    }
}
