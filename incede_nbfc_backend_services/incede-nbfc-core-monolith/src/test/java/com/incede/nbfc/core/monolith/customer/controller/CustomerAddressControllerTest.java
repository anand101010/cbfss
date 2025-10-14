package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAddressService;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CustomerAddressControllerTest {

    @Mock
    private CustomerAddressService customerAddressService;

    @InjectMocks
    private CustomerAddressController controller;

    private UUID customerIdentity;
    private UUID addressIdentity;
    private UUID addressType;
    private UUID addressProofType;
    private UUID postOffice;
    private CustomerAddressRequestDto requestDto;
    private CustomerAddressResponseDto responseDto;
    private CustomerAddressResponseDto.AddressDetail addressDetail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerIdentity = UUID.randomUUID();
        addressIdentity = UUID.randomUUID();
        addressType = UUID.randomUUID();
        addressProofType = UUID.randomUUID();
        postOffice = UUID.randomUUID();

        requestDto = CustomerAddressRequestDto.builder()
                .addressType(addressType)
                .doorNumber("123")
                .addressLine1("Main Street")
                .addressLine2("Central District")
                .landmark("Near City Mall")
                .placeName("Downtown")
                .city("Metropolis")
                .district("Central District")
                .state("State")
                .country("Country")
                .pincode("123456")
                .latitude(new BigDecimal("40.7128"))
                .longitude(new BigDecimal("-74.0060"))
                .geoAccuracy(new BigDecimal("95.5"))
                .addressProofType(addressProofType)
                .isActive(true)
                .digipin("1234")
                .documentRefId("DOC_REF_001")
                .filePath("/documents/address_proof.pdf")
                .build();

        addressDetail = CustomerAddressResponseDto.AddressDetail.builder()
                .addressIdentity(addressIdentity)
                .addressType(addressType)
                .doorNumber("123")
                .addressLine1("Main Street")
                .addressLine2("Central District")
                .landmark("Near City Mall")
                .placeName("Downtown")
                .city("Metropolis")
                .district("Central District")
                .state("State")
                .country("Country")
                .pincode("123456")
                .postOffice(postOffice)
                .latitude(new BigDecimal("40.7128"))
                .longitude(new BigDecimal("-74.0060"))
                .geoAccuracy(new BigDecimal("95.5"))
                .addressProofType(addressProofType)
                .isActive(true)
                .digipin("1234")
                .documentRefId("DOC_REF_001")
                .filePath("/documents/address_proof.pdf")
                .build();

        responseDto = CustomerAddressResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .status("ACTIVE")
                .addresses(List.of(addressDetail))
                .build();
    }

    @Test
    void testCreateAddress_Success() {
        when(customerAddressService.createAddress(eq(customerIdentity), any(CustomerAddressRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerAddressResponseDto> result =
                controller.createAddress(customerIdentity, requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
        assertNotNull(result.getBody().getAddresses());
        assertEquals(1, result.getBody().getAddresses().size());
        assertEquals(addressIdentity, result.getBody().getAddresses().get(0).getAddressIdentity());
        verify(customerAddressService, times(1)).createAddress(eq(customerIdentity), any(CustomerAddressRequestDto.class));
    }

    @Test
    void testUpdateAddress_Success() {
        when(customerAddressService.updateAddress(eq(customerIdentity), eq(addressIdentity), any(CustomerAddressRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerAddressResponseDto> result =
                controller.updateAddress(customerIdentity, addressIdentity, requestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
        assertNotNull(result.getBody().getAddresses());
        assertEquals(1, result.getBody().getAddresses().size());
        verify(customerAddressService, times(1)).updateAddress(eq(customerIdentity), eq(addressIdentity), any(CustomerAddressRequestDto.class));
    }

    @Test
    void testGetActiveAddresses_Success() {
        when(customerAddressService.getActiveAddressesByCustomerIdentity(eq(customerIdentity)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerAddressResponseDto> result =
                controller.getActiveAddresses(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
        assertNotNull(result.getBody().getAddresses());
        assertEquals(1, result.getBody().getAddresses().size());

        CustomerAddressResponseDto.AddressDetail detail = result.getBody().getAddresses().get(0);
        assertTrue(detail.getIsActive());
        assertEquals("Main Street", detail.getAddressLine1());
        assertEquals("Metropolis", detail.getCity());
        assertEquals("123456", detail.getPincode());

        verify(customerAddressService, times(1)).getActiveAddressesByCustomerIdentity(eq(customerIdentity));
    }

    @Test
    void testGetActiveAddresses_MultipleAddresses() {
        CustomerAddressResponseDto.AddressDetail addressDetail2 = CustomerAddressResponseDto.AddressDetail.builder()
                .addressIdentity(UUID.randomUUID())
                .addressType(addressType)
                .doorNumber("456")
                .addressLine1("Second Street")
                .city("Metropolis")
                .pincode("123457")
                .isActive(true)
                .build();

        CustomerAddressResponseDto responseWithMultipleAddresses = CustomerAddressResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .status("ACTIVE")
                .addresses(List.of(addressDetail, addressDetail2))
                .build();

        when(customerAddressService.getActiveAddressesByCustomerIdentity(eq(customerIdentity)))
                .thenReturn(responseWithMultipleAddresses);

        ResponseEntity<CustomerAddressResponseDto> result =
                controller.getActiveAddresses(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody().getAddresses());
        assertEquals(2, result.getBody().getAddresses().size());

        // Verify both addresses are active
        assertTrue(result.getBody().getAddresses().get(0).getIsActive());
        assertTrue(result.getBody().getAddresses().get(1).getIsActive());
    }

    @Test
    void testDeleteAddress_Success() {
        doNothing().when(customerAddressService).deleteAddress(eq(customerIdentity), eq(addressIdentity));

        ResponseEntity<Void> result =
                controller.deleteAddress(customerIdentity, addressIdentity);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
        verify(customerAddressService, times(1)).deleteAddress(eq(customerIdentity), eq(addressIdentity));
    }

    @Test
    void testCreateAddress_BusinessException() {
        when(customerAddressService.createAddress(eq(customerIdentity), any(CustomerAddressRequestDto.class)))
                .thenThrow(new BusinessException("Customer not found", ErrorCodes.RESOURCE_NOT_FOUND));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createAddress(customerIdentity, requestDto)
        );

        assertEquals("Customer not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        verify(customerAddressService, times(1)).createAddress(eq(customerIdentity), any(CustomerAddressRequestDto.class));
    }

    @Test
    void testUpdateAddress_BusinessException() {
        when(customerAddressService.updateAddress(eq(customerIdentity), eq(addressIdentity), any(CustomerAddressRequestDto.class)))
                .thenThrow(new BusinessException("Address not found", ErrorCodes.RESOURCE_NOT_FOUND));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.updateAddress(customerIdentity, addressIdentity, requestDto)
        );

        assertEquals("Address not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        verify(customerAddressService, times(1)).updateAddress(eq(customerIdentity), eq(addressIdentity), any(CustomerAddressRequestDto.class));
    }

    @Test
    void testGetActiveAddresses_BusinessException() {
        when(customerAddressService.getActiveAddressesByCustomerIdentity(eq(customerIdentity)))
                .thenThrow(new BusinessException("Customer not found", ErrorCodes.RESOURCE_NOT_FOUND));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.getActiveAddresses(customerIdentity)
        );

        assertEquals("Customer not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        verify(customerAddressService, times(1)).getActiveAddressesByCustomerIdentity(eq(customerIdentity));
    }

    @Test
    void testDeleteAddress_BusinessException() {
        doThrow(new BusinessException("Address not found", ErrorCodes.RESOURCE_NOT_FOUND))
                .when(customerAddressService).deleteAddress(eq(customerIdentity), eq(addressIdentity));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.deleteAddress(customerIdentity, addressIdentity)
        );

        assertEquals("Address not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
        verify(customerAddressService, times(1)).deleteAddress(eq(customerIdentity), eq(addressIdentity));
    }

    @Test
    void testCreateAddress_ValidationException() {
        CustomerAddressRequestDto invalidRequest = CustomerAddressRequestDto.builder()
                .addressLine1("")
                .city("")
                .pincode("invalid")
                .build();

        when(customerAddressService.createAddress(eq(customerIdentity), any(CustomerAddressRequestDto.class)))
                .thenThrow(new BusinessException("Invalid address data", ErrorCodes.VALIDATION_FAILED));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createAddress(customerIdentity, invalidRequest)
        );

        assertEquals("Invalid address data", ex.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
    }

    @Test
    void testUpdateAddress_ConflictException() {
        when(customerAddressService.updateAddress(eq(customerIdentity), eq(addressIdentity), any(CustomerAddressRequestDto.class)))
                .thenThrow(new BusinessException("Address update conflict", ErrorCodes.CONFLICT));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.updateAddress(customerIdentity, addressIdentity, requestDto)
        );

        assertEquals("Address update conflict", ex.getMessage());
        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
    }

    @Test
    void testGetActiveAddresses_EmptyAddressList() {
        CustomerAddressResponseDto emptyResponse = CustomerAddressResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .status("ACTIVE")
                .addresses(List.of())
                .build();

        when(customerAddressService.getActiveAddressesByCustomerIdentity(eq(customerIdentity)))
                .thenReturn(emptyResponse);

        ResponseEntity<CustomerAddressResponseDto> result =
                controller.getActiveAddresses(customerIdentity);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody().getAddresses());
        assertTrue(result.getBody().getAddresses().isEmpty());
    }


}