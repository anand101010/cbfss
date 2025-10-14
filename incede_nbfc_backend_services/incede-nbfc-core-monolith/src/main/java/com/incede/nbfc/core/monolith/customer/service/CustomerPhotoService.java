package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPhoto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerPhotoMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerPhotoRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing customer photos.
 * Handles create and fetch operations for customer photos.
 * Ensures exception handling is consistent with other services.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerPhotoService {

    private final CustomerRepository customerRepository;
    private final CustomerPhotoRepository photoRepository;
    private final CustomerPhotoMapper customerPhotoMapper;
    private final UserRepository userRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Create and store a new customer photo.
     *
     * @param identity UUID of the customer
     * @param customerPhotoRequestDto DTO containing photo details
     * @return Response DTO with customer photo details
     */
    @Transactional
    public CustomerPhotoResponseDto createPhoto(UUID identity, CustomerPhotoRequestDto customerPhotoRequestDto) {
        try {
            Customer customer = customerRepository.findByIdentity(identity)
                    .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));

            // Validate the request DTO
            var violations = validator.validate(customerPhotoRequestDto);
            if (!violations.isEmpty()) {
                String errorMsg = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .orElse(CommonConstants.INVALID_REQUEST);
                throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
            }

            User capturedBy = userRepository.findByIdentity(customerPhotoRequestDto.getCapturedBy())
                    .orElseThrow(() -> new BusinessException(CommonConstants.CAPTURED_BY_NOT_FOUND, ErrorCodes.NOT_FOUND));

            CustomerPhoto photo = customerPhotoMapper.toEntity(customerPhotoRequestDto);
            photo.setCustomer(customer);
            photo.setCapturedBy(capturedBy);

            CustomerPhoto savedPhoto = photoRepository.save(photo);

            return customerPhotoMapper.toResponseDto(customer, List.of(savedPhoto));

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving photo for customer {}. DTO: {}", identity, customerPhotoRequestDto, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        }
    }

    /**
     * Fetch all photos for a given customer.
     *
     * @param identity UUID of the customer
     * @return Response DTO with photo details
     */
    @Transactional(readOnly = true)
    public CustomerPhotoResponseDto getCustomerPhotos(UUID identity) {
        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));

        List<CustomerPhoto> photos = photoRepository
                .findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(identity);

        if (photos.isEmpty()) {
            log.warn("No photos found for customer {}", identity);
            throw new ResourceNotFoundException(CommonConstants.PHOTOS_NOT_FOUND);
        }

        return customerPhotoMapper.toResponseDto(customer, photos);
    }
}