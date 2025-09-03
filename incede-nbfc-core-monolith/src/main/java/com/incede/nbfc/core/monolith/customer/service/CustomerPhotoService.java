package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPhoto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerPhotoRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerPhotoService {
    private final CustomerRepository customerRepository;
    private final CustomerPhotoRepository photoRepository;

    @Transactional
    public CustomerPhotoResponseDto createPhoto(UUID identity, CustomerPhotoRequestDto requestDTO) {

        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", identity.toString()));

        CustomerPhoto photo = new CustomerPhoto();
        photo.setCustomer(customer);
        photo.setPhotoRefId(generatePhotoRefId());
        photo.setLatitude(requestDTO.getLatitude());
        photo.setAccuracy(requestDTO.getAccuracy());
        photo.setCapturedBy(requestDTO.getCapturedBy());
        photo.setCaptureDevice(requestDTO.getCaptureDevice());
        photo.setCaptureTime(requestDTO.getCaptureTime());
        photo.setFilePath(requestDTO.getFilePath());
        photo.setLocationDescription(requestDTO.getLocationDescription());
        photo.setLongitude(requestDTO.getLongitude());
        photo.setStatus(requestDTO.getStatus());
        photo.setCreatedBy(requestDTO.getCreatedBy());
        photo.setUpdatedBy(requestDTO.getUpdatedBy());

        CustomerPhoto savedPhoto = photoRepository.save(photo);

        CustomerPhotoResponseDto.PhotoDetail detail = CustomerPhotoResponseDto.PhotoDetail.builder()
                .photoId(savedPhoto.getPhotoId())
                .photoRefId(savedPhoto.getPhotoRefId())
                .status(savedPhoto.getStatus())
                .captureTime(savedPhoto.getCaptureTime())
                .captureTime(savedPhoto.getCaptureTime())
                .build();

        return CustomerPhotoResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status("IN_PROGRESS")
                .photo(List.of(detail))
                .build();

    }

    public Integer generatePhotoRefId() {
        return UUID.randomUUID().hashCode();
    }
    @Transactional
    public CustomerPhotoResponseDto getCustomerPhotos(UUID identity) {
        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", identity.toString()));

        List<CustomerPhoto> photos = photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(identity);

        if (photos.isEmpty()) {
            throw new ResourceNotFoundException("CustomerPhoto", identity.toString());
        }

        List<CustomerPhotoResponseDto.PhotoDetail> details = photos.stream()
                .map(photo -> CustomerPhotoResponseDto.PhotoDetail.builder()
                        .photoId(photo.getPhotoId())
                        .photoRefId(photo.getPhotoRefId())
                        .status(photo.getStatus())
                        .captureTime(photo.getCaptureTime())

                        .build())
                .toList();

        return CustomerPhotoResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .status(customer.getOnboardingStatus() != null ? customer.getOnboardingStatus() : "IN_PROGRESS")
                .photo(details)
                .build();
    }


}
