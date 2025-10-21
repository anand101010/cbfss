package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerNotificationPreference;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerNotificationPreferenceMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerNotificationPreferenceRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationPreferenceService {

    private final CustomerRepository customerRepository;
    private final CustomerNotificationPreferenceRepository notificationRepository;
    private final CustomerNotificationPreferenceMapper mapper = new CustomerNotificationPreferenceMapper();

    /**
     * save notification preference
     * @param customerId
     * @param dto
     * @return
     */
    @Transactional
    public CustomerNotificationPreferenceResponseDto saveNotification(UUID customerId, CustomerNotificationPreferenceRequestDto dto) {
        log.info("Saving notification preference for customer [{}]", customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        if (notificationRepository.findByCustomer(customer).isPresent()) {
            throw new BusinessException(CommonConstants.NOTIFICATION_PREFERENCE_ALREADY_EXIST, ErrorCodes.CONFLICT);
        }

        CustomerNotificationPreference entity = mapper.toEntity(dto, customer);
        CustomerNotificationPreference saved = notificationRepository.save(entity);
        return mapper.toResponseDto(saved);
    }

    /**
     * update notification preference
     * @param customerId
     * @param dto
     * @return
     */

    @Transactional
    public CustomerNotificationPreferenceResponseDto updateNotification(UUID customerId, CustomerNotificationPreferenceRequestDto dto) {
        log.info("Updating notification preference for customer [{}]", customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerNotificationPreference entity = notificationRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOTIFICATION_PREFERENCE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        mapper.updateEntityFromDto(entity, dto);
        CustomerNotificationPreference updated = notificationRepository.save(entity);
        return mapper.toResponseDto(updated);
    }


    /**
     * get notification preference
     * @param customerId
     * @return
     */


    @Transactional(readOnly = true)
    public CustomerNotificationPreferenceResponseDto getNotificationPreferences(UUID customerId) {
        log.info("Fetching notification preferences for customer [{}]", customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerNotificationPreference entity = notificationRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException(CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        return mapper.toResponseDto(entity);
    }

    /**
     * Soft delete Customers Notification Preference
     * @param customerId
     * @return
     */
    @Transactional
    public CustomerNotificationPreferenceResponseDto deleteNotificationPreference(UUID customerId) {
        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        CustomerNotificationPreference notificationPreference = notificationRepository.findByCustomer(customer)
                .orElseThrow(() -> new ResourceNotFoundException("Notification preference not found for customer id: " + customerId));

        notificationRepository.delete(notificationPreference);
        return mapper.toResponseDto(notificationPreference);
    }


}
