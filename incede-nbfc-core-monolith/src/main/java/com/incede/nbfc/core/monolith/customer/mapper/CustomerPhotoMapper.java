package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPhoto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between CustomerPhoto entities and DTOs.
 *
 * Implemented manually as a Spring @Component instead of MapStruct.
 */
@Component
public class CustomerPhotoMapper {

    /**
     * Converts a request DTO into a CustomerPhoto entity.
     *
     * @param dto request DTO
     * @return mapped entity
     */
    public CustomerPhoto toEntity(CustomerPhotoRequestDto dto) {
        java.util.Objects.requireNonNull(dto, "CustomerPhoto must not be null");

        CustomerPhoto photo = new CustomerPhoto();
        photo.setLatitude(dto.getLatitude());
        photo.setAccuracy(dto.getAccuracy());
        photo.setCapturedBy(dto.getCapturedBy());
        photo.setCaptureDevice(dto.getCaptureDevice());
        photo.setCaptureTime(LocalDateTime.parse(dto.getCaptureTime()));

        photo.setFilePath(dto.getFilePath());
        photo.setLocationDescription(dto.getLocationDescription());
        photo.setLongitude(dto.getLongitude());
        photo.setStatus(dto.getStatus());
        photo.setCreatedBy(dto.getCreatedBy());
        photo.setUpdatedBy(dto.getUpdatedBy());
        photo.setIsDel(false);
        return photo;
    }

    /**
     * Converts a single CustomerPhoto entity to PhotoDetail DTO.
     *
     * @param entity photo entity
     * @return photo detail DTO
     */
    public CustomerPhotoResponseDto.PhotoDetail toPhotoDetail(CustomerPhoto entity,Customer customer) {
        if (entity == null) {
            return null;
        }
        return CustomerPhotoResponseDto.PhotoDetail.builder()
                .photoId(entity.getPhotoId())
                .firstname(customer.getFirstName())
                .photoRefId(entity.getPhotoRefId())
                .status(entity.getStatus())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .capturedBy(entity.getCapturedBy())
                .captureTime(entity.getCaptureTime())
                .accuracy(entity.getAccuracy())
                .captureDevice(entity.getCaptureDevice())
                .filePath(entity.getFilePath())
                .locationDescription(entity.getLocationDescription())
                .build();
    }

    /**
     * Converts a list of CustomerPhoto entities to PhotoDetail DTOs.
     *
     * @param entities list of photo entities
     * @return list of photo detail DTOs
     */
    public List<CustomerPhotoResponseDto.PhotoDetail> toPhotoDetails(List<CustomerPhoto> entities,Customer customer) {
        return entities == null ? List.of() :
                entities.stream().map(photo -> toPhotoDetail(photo, customer)).collect(Collectors.toList());
    }

    /**
     * Builds the response DTO from customer + photos.
     *
     * @param customer customer entity
     * @param photos   list of photo entities
     * @return response DTO
     */
    public CustomerPhotoResponseDto toResponseDto(Customer customer, List<CustomerPhoto> photos) {
        return CustomerPhotoResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())


                .status(customer.getOnboardingStatus() != null ? customer.getOnboardingStatus() : "IN_PROGRESS")
                .photo(toPhotoDetails(photos,customer))

                .build();
    }
}
