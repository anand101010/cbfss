package com.incede.nbfc.core.monolith.customer.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    private final ObjectMapper objectMapper; // Injected ObjectMapper

    /**
     * Create and store a new customer photo.
     *
     * @param identity   UUID of the customer
     * @param requestJson DTO containing photo details
     * @return Response DTO with customer photo details
     */
    @Transactional
    public CustomerPhotoResponseDto createPhoto(UUID identity, String requestJson, MultipartFile file) {
        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.NOT_FOUND_MESSAGE));

        try {
            CustomerPhotoRequestDto requestDTO = objectMapper.readValue(requestJson, CustomerPhotoRequestDto.class);

            CustomerPhoto photo = customerPhotoMapper.toEntity(requestDTO);
            photo.setCustomer(customer);

            Integer photoRefId = uploadPhoto(file);
            photo.setPhotoRefId(photoRefId);
            CustomerPhoto savedPhoto = photoRepository.save(photo);
            return customerPhotoMapper.toResponseDto(customer, List.of(savedPhoto));

        } catch (JsonProcessingException e) {
            log.warn("Invalid JSON for photo request: {} - {}", requestJson, e.getMessage());
            throw new BusinessException(CommonConstants.INVALID_JSON, ErrorCodes.VALIDATION_FAILED, e);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving photo for customer {}. JSON: {}", identity, requestJson, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);

        } catch (IOException e) {
            log.error("File processing failed for customer {}. File: {}", identity, file.getOriginalFilename(), e);
            throw new BusinessException("Error processing file upload", ErrorCodes.INTERNAL_SERVER_ERROR, e);

        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for photo DTO: {} - {}", requestJson, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
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
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.NOT_FOUND_MESSAGE));

        List<CustomerPhoto> photos = photoRepository
                .findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(identity);

        if (photos.isEmpty()) {
            log.warn("No photos found for customer {}", identity);
            throw new ResourceNotFoundException(CommonConstants.NOT_FOUND_MESSAGE);
        }

        return customerPhotoMapper.toResponseDto(customer, photos);
    }

    /**
     * Generate a unique photo reference ID.
     *
     * @return Generated reference ID
     */
    public Integer uploadPhoto(MultipartFile file) {
        return Math.abs(UUID.randomUUID().hashCode());
    }

}

