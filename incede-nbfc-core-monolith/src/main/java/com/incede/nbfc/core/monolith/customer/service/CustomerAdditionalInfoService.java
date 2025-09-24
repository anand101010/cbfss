//package com.incede.nbfc.core.monolith.customer.service;
//
//import com.incede.nbfc.core.monolith.common.CommonConstants;
//import com.incede.nbfc.core.monolith.customer.domain.entity.*;
//import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoRequestDto;
//import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalInfoResponseDto;
//import com.incede.nbfc.core.monolith.customer.mapper.CustomerAdditionalInfoMapper;
//import com.incede.nbfc.core.monolith.customer.repository.*;
//import com.incede.nbfc.core.monolith.exception.BusinessException;
//import com.incede.nbfc.core.monolith.exception.ErrorCodes;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//
///**
// * Service class for managing additional customer information such as employment,
// * referrals, PEP status, profile extras, and assets.
// * Provides methods to create, update, and fetch all related additional info for a customer.
// */
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class CustomerAdditionalInfoService {
//
//    private final CustomerAdditionalInfoMapper customerAdditionalInfoMapper;
//
//    private final CustomerRepository customerRepository;
//    private final CustomerEmploymentRepository employmentRepository;
//    private final CustomerReferralRepository referralRepository;
//    private final CustomerPepRepository pepRepository;
//    private final CustomerProfileExtraRepository profileExtraRepository;
//    private final CustomerAssetRepository assetRepository;
//
//
//    /**
//     * Save additional information for a given customer. Creates or updates sub-entities
//     * such as employment, referrals, PEP, profile extras, and assets.
//     *
//     * @param identity the UUID of the customer
//     * @param dto      DTO containing the additional info to be saved
//     * @return CustomerAdditionalInfoResponseDto containing saved info
//     * @throws BusinessException if customer not found or persistence fails
//     */
//    @Transactional
//    public CustomerAdditionalInfoResponseDto saveAdditionalInfo(UUID identity, CustomerAdditionalInfoRequestDto dto) {
//        log.info("Saving additional info for customer with identity: {}", identity);
//
//        Customer customer = customerRepository.findByIdentity(identity)
//                .orElseThrow(() -> {
//                    log.warn("Customer not found while saving additional info. identity={}", identity);
//                    return new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND);
//                });
//
//        try {
//            CustomerEmployment employment = employmentRepository.findByCustomer(customer)
//                    .orElseGet(CustomerEmployment::new);
//            customerAdditionalInfoMapper.createEmployment(employment, dto.getAdditional().getEmployment(), customer);
//            employmentRepository.save(employment);
//
//            CustomerReferral referral = referralRepository.findByCustomer(customer)
//                    .orElseGet(CustomerReferral::new);
//            customerAdditionalInfoMapper.createReferral(referral, dto.getAdditional().getReferrals(), customer);
//            referralRepository.save(referral);
//
//            CustomerPep pep = pepRepository.findByCustomer(customer)
//                    .orElseGet(CustomerPep::new);
//            customerAdditionalInfoMapper.createPep(pep, dto.getAdditional().getPep(), customer);
//            pepRepository.save(pep);
//
//            CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer)
//                    .orElseGet(CustomerProfileExtra::new);
//            customerAdditionalInfoMapper.createProfileExtra(profileExtra, dto.getAdditional().getProfileExtra(), customer);
//            profileExtraRepository.save(profileExtra);
//
//            CustomerAsset asset = assetRepository.findByCustomer(customer)
//                    .orElseGet(CustomerAsset::new);
//            customerAdditionalInfoMapper.createAsset(asset, dto.getAdditional().getCustomerAsset(), customer);
//            assetRepository.save(asset);
//
//            Customer updatedCustomer = customerAdditionalInfoMapper
//                    .updateCustomerFromAdditionalInfo(customer, dto.getAdditional().getCustomer());
//            customerRepository.save(updatedCustomer);
//
//            log.info("Successfully saved additional info for customer: {}", identity);
//
//            return customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, pep, profileExtra, asset);
//
//        } catch (DataIntegrityViolationException e) {
//            log.error("Constraint violation while saving additional info for customer: {} DTO: {}", identity, dto, e);
//            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
//                    ErrorCodes.CONSTRAINT_VIOLATION, e);
//        } catch (IllegalArgumentException e) {
//            log.warn("Validation failed while saving additional info. DTO: {} - {}", dto, e.getMessage());
//            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
//        }
//    }
//
//
//    /**
//     * Update existing additional information for a given customer.
//     *
//     * @param identity the UUID of the customer
//     * @param dto      DTO containing updated additional info
//     * @return CustomerAdditionalInfoResponseDto with updated info
//     * @throws BusinessException if any required entity not found or persistence fails
//     */
//    @Transactional
//    public CustomerAdditionalInfoResponseDto updateAdditionalInfo(UUID identity, CustomerAdditionalInfoRequestDto dto) {
//        log.info("Updating additional info for customer with identity: {}", identity);
//
//        Customer customer = customerRepository.findByIdentity(identity)
//                .orElseThrow(() -> {
//                    log.warn("Customer not found while updating additional info. identity={}", identity);
//                    return new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND);
//                });
//
//        try {
//            CustomerEmployment employment = employmentRepository.findByCustomer(customer)
//                    .orElseThrow(() -> new BusinessException(CommonConstants.EMPLOYMENT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
//            customerAdditionalInfoMapper.updateEmployment(employment, dto.getAdditional().getEmployment(), customer);
//            employmentRepository.save(employment);
//
//            CustomerReferral referral = referralRepository.findByCustomer(customer)
//                    .orElseThrow(() -> new BusinessException(CommonConstants.REFERRAL_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
//            customerAdditionalInfoMapper.updateReferral(referral, dto.getAdditional().getReferrals(), customer);
//            referralRepository.save(referral);
//
//            CustomerPep pep = pepRepository.findByCustomer(customer)
//                    .orElseThrow(() -> new BusinessException(CommonConstants.PEP_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
//            customerAdditionalInfoMapper.updatePep(pep, dto.getAdditional().getPep(), customer);
//            pepRepository.save(pep);
//
//            CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer)
//                    .orElseThrow(() -> new BusinessException(CommonConstants.PROFILE_EXTRA_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
//            customerAdditionalInfoMapper.updateProfileExtra(profileExtra, dto.getAdditional().getProfileExtra(), customer);
//            profileExtraRepository.save(profileExtra);
//
//            CustomerAsset asset = assetRepository.findByCustomer(customer)
//                    .orElseThrow(() -> new BusinessException(CommonConstants.ASSET_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
//            log.warn("Customer asset info not found for identity={}", identity);
//
//            customerAdditionalInfoMapper.updateAsset(asset,dto.getAdditional().getCustomerAsset(), customer);
//            assetRepository.save(asset);
//
//            Customer updatedCustomer = customerAdditionalInfoMapper
//                    .updateCustomerFromAdditionalInfo(customer, dto.getAdditional().getCustomer());
//            customerRepository.save(updatedCustomer);
//
//            log.info("Successfully updated additional info for customer: {}", identity);
//
//            return customerAdditionalInfoMapper.buildResponseDto(updatedCustomer, employment, referral, pep, profileExtra, asset);
//
//        } catch (DataIntegrityViolationException e) {
//            log.error("Constraint violation while updating additional info for customer: {} DTO: {}", identity, dto, e);
//            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
//                    ErrorCodes.CONSTRAINT_VIOLATION, e);
//        } catch (IllegalArgumentException e) {
//            log.warn("Validation failed while updating additional info. DTO: {} - {}", dto, e.getMessage());
//            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
//        }
//    }
//
//
//    /**
//     * Fetch all additional information for a given customer by identity.
//     *
//     * @param customerIdentity UUID of the customer
//     * @return CustomerAdditionalInfoResponseDto containing all related details
//     * @throws BusinessException if customer not found
//     */
//    @Transactional(readOnly = true)
//    public CustomerAdditionalInfoResponseDto getAdditionalInfo(UUID customerIdentity) {
//        log.info("Fetching additional info for customer with identity: {}", customerIdentity);
//
//        Customer customer = customerRepository.findByIdentity(customerIdentity)
//                .orElseThrow(() -> {
//                    log.warn("Customer not found while fetching additional info. identity={}", customerIdentity);
//                    return new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND);
//                });
//
//        CustomerEmployment employment = employmentRepository.findByCustomer(customer).orElse(null);
//        CustomerReferral referral = referralRepository.findByCustomer(customer).orElse(null);
//        CustomerPep pep = pepRepository.findByCustomer(customer).orElse(null);
//        CustomerProfileExtra profileExtra = profileExtraRepository.findByCustomer(customer).orElse(null);
//        CustomerAsset assets = assetRepository.findByCustomer(customer).orElse(null);
//
//        log.info("Successfully fetched additional info for customer: {}", customerIdentity);
//        return customerAdditionalInfoMapper.buildResponseDto(customer, employment, referral, pep, profileExtra, assets);
//    }
//}
