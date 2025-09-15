package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAddressControllerTest {

    @Mock
    private CustomerAddressService customerAddressService;

    @InjectMocks
    private CustomerAddressController customerAddressController;

    private UUID customerIdentity;
    private Integer addressId;
    private CustomerAddressRequestDto requestDTO;
    private CustomerAddressResponseDto responseDTO;

    @BeforeEach
    void setUp() {
        customerIdentity = UUID.fromString("123e4567-e89b-12d3-a456-426614174003");
        addressId = 1;

        requestDTO = CustomerAddressRequestDto.builder()
                .addressTypeId(1)
                .doorNumber("12C")
                .addressLine1("MG Road")
                .addressLine2("Near Central Park")
                .landmark("City Mall")
                .placeName("Downtown")
                .cityId(101)
                .districtId(10)
                .stateId(5)
                .countryId(1)
                .pincode(560001)
                .postOfficeId(25)
                .latitude(new BigDecimal("12.971598"))
                .longitude(new BigDecimal("77.594566"))
                .geoAccuracy(new BigDecimal("5.50"))
                .addressProofType(2)
                .isActive(true)
                .digipin("DIGI12405")
                .build();

        responseDTO = CustomerAddressResponseDto.builder()
                .identity(customerIdentity)
                .customerCode("CUST001")
                .status("SUCCESS")
                .build();
    }

    @Test
    void testCreateAddressSuccess() {
        when(customerAddressService.createAddress(customerIdentity, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<CustomerAddressResponseDto> response =
                customerAddressController.createAddress(customerIdentity, requestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerIdentity, response.getBody().getIdentity());
        assertEquals("CUST001", response.getBody().getCustomerCode());

        verify(customerAddressService).createAddress(customerIdentity, requestDTO);
    }

    @Test
    void testUpdateAddressSuccess() {
        when(customerAddressService.updateAddress(customerIdentity, addressId, requestDTO)).thenReturn(responseDTO);

        ResponseEntity<CustomerAddressResponseDto> response =
                customerAddressController.updateAddress(customerIdentity, addressId, requestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerIdentity, response.getBody().getIdentity());

        verify(customerAddressService).updateAddress(customerIdentity, addressId, requestDTO);
    }

    @Test
    void testUpdateAddressWithDifferentAddressIds() {
        Integer[] addressIds = {1, 2, 100, 999};

        for (Integer id : addressIds) {
            when(customerAddressService.updateAddress(customerIdentity, id, requestDTO)).thenReturn(responseDTO);

            ResponseEntity<CustomerAddressResponseDto> response =
                    customerAddressController.updateAddress(customerIdentity, id, requestDTO);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(customerAddressService).updateAddress(customerIdentity, id, requestDTO);
        }
    }

    @Test
    void testGetActiveAddressesSuccess() {
        when(customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity)).thenReturn(responseDTO);

        ResponseEntity<CustomerAddressResponseDto> response =
                customerAddressController.getActiveAddresses(customerIdentity);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerIdentity, response.getBody().getIdentity());

        verify(customerAddressService).getActiveAddressesByCustomerIdentity(customerIdentity);
    }

    @Test
    void testDeleteAddressSuccess() {
        doNothing().when(customerAddressService).deleteAddress(customerIdentity, addressId);

        ResponseEntity<Void> response =
                customerAddressController.deleteAddress(customerIdentity, addressId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(customerAddressService).deleteAddress(customerIdentity, addressId);
    }

    @Test
    void testCreateAddressWithDifferentUUIDs() {
        UUID[] uuids = {
                UUID.fromString("00000000-0000-0000-0000-000000000000"),
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                UUID.randomUUID()
        };

        for (UUID uuid : uuids) {
            when(customerAddressService.createAddress(uuid, requestDTO)).thenReturn(responseDTO);

            ResponseEntity<CustomerAddressResponseDto> response =
                    customerAddressController.createAddress(uuid, requestDTO);

            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            verify(customerAddressService).createAddress(uuid, requestDTO);
        }
    }

    @Test
    void testUpdateAddressWithDifferentUUIDs() {
        UUID[] uuids = {
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                UUID.randomUUID()
        };

        for (UUID uuid : uuids) {
            when(customerAddressService.updateAddress(uuid, addressId, requestDTO)).thenReturn(responseDTO);

            ResponseEntity<CustomerAddressResponseDto> response =
                    customerAddressController.updateAddress(uuid, addressId, requestDTO);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(customerAddressService).updateAddress(uuid, addressId, requestDTO);
        }
    }

    @Test
    void testGetActiveAddressesWithDifferentUUIDs() {
        UUID[] uuids = {
                UUID.fromString("44444444-4444-4444-4444-444444444444"),
                UUID.fromString("55555555-5555-5555-5555-555555555555"),
                UUID.randomUUID()
        };

        for (UUID uuid : uuids) {
            when(customerAddressService.getActiveAddressesByCustomerIdentity(uuid)).thenReturn(responseDTO);

            ResponseEntity<CustomerAddressResponseDto> response =
                    customerAddressController.getActiveAddresses(uuid);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(customerAddressService).getActiveAddressesByCustomerIdentity(uuid);
        }
    }

    @Test
    void testDeleteAddress_WithDifferentAddressIds() {
        Integer[] addressIds = {1, 2, 100, 999};

        for (Integer id : addressIds) {
            doNothing().when(customerAddressService).deleteAddress(customerIdentity, id);

            ResponseEntity<Void> response =
                    customerAddressController.deleteAddress(customerIdentity, id);

            assertNotNull(response);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(customerAddressService).deleteAddress(customerIdentity, id);
        }
    }

    @Test
    void testDeleteAddress_WithDifferentUUIDsAndAddressIds() {
        UUID[] uuids = {
                UUID.fromString("66666666-6666-6666-6666-666666666666"),
                UUID.fromString("77777777-7777-7777-7777-777777777777")
        };

        Integer[] addressIds = {10, 20};

        for (UUID uuid : uuids) {
            for (Integer id : addressIds) {
                doNothing().when(customerAddressService).deleteAddress(uuid, id);

                ResponseEntity<Void> response =
                        customerAddressController.deleteAddress(uuid, id);

                assertNotNull(response);
                assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
                verify(customerAddressService).deleteAddress(uuid, id);
            }
        }
    }

    @Test
    void testControllerMethodsWithNullServiceResponse() {

        when(customerAddressService.createAddress(customerIdentity, requestDTO)).thenReturn(null);
        ResponseEntity<CustomerAddressResponseDto> createResponse =
                customerAddressController.createAddress(customerIdentity, requestDTO);
        assertNotNull(createResponse);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNull(createResponse.getBody());


        when(customerAddressService.updateAddress(customerIdentity, addressId, requestDTO)).thenReturn(null);
        ResponseEntity<CustomerAddressResponseDto> updateResponse =
                customerAddressController.updateAddress(customerIdentity, addressId, requestDTO);
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNull(updateResponse.getBody());


        when(customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity)).thenReturn(null);
        ResponseEntity<CustomerAddressResponseDto> getResponse =
                customerAddressController.getActiveAddresses(customerIdentity);
        assertNotNull(getResponse);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNull(getResponse.getBody());
    }

    @Test
    void testControllerMethods_VerifyServiceCallCount() {

        when(customerAddressService.createAddress(customerIdentity, requestDTO)).thenReturn(responseDTO);
        customerAddressController.createAddress(customerIdentity, requestDTO);
        verify(customerAddressService, times(1)).createAddress(customerIdentity, requestDTO);


        when(customerAddressService.updateAddress(customerIdentity, addressId, requestDTO)).thenReturn(responseDTO);
        customerAddressController.updateAddress(customerIdentity, addressId, requestDTO);
        verify(customerAddressService, times(1)).updateAddress(customerIdentity, addressId, requestDTO);


        when(customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity)).thenReturn(responseDTO);
        customerAddressController.getActiveAddresses(customerIdentity);
        verify(customerAddressService, times(1)).getActiveAddressesByCustomerIdentity(customerIdentity);


        doNothing().when(customerAddressService).deleteAddress(customerIdentity, addressId);
        customerAddressController.deleteAddress(customerIdentity, addressId);
        verify(customerAddressService, times(1)).deleteAddress(customerIdentity, addressId);
    }
}
