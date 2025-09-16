package com.incede.nbfc.core.monolith.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerAddressControllerTest {

    @Mock
    private CustomerAddressService customerAddressService;

    @InjectMocks
    private CustomerAddressController controller;

    private UUID customerId;
    private UUID addressId;
    private String requestJson;
    private CustomerAddressResponseDto responseDto;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        // Example JSON payload as string
        requestJson = """
                {
                  "addressTypeId": 1,
                  "doorNumber": "12C",
                  "addressLine1": "MG Road",
                  "addressLine2": "Near Central",
                  "landmark": "City Mall",
                  "placeName": "Downtown",
                  "cityId": 101,
                  "districtId": 10,
                  "stateId": 5,
                  "countryId": 1,
                  "pincode": 560001,
                  "postOfficeId": 25,
                  "latitude": null,
                  "longitude": null,
                  "geoAccuracy": null,
                  "addressProofType": 2,
                  "isActive": true,
                  "createdBy": 1,
                  "updatedBy": 1,
                  "isSameAsPermanent": false,
                  "digipin": "DIG210003",
                  "customerCode": "CUST1001"
                }
                """;

        responseDto = new CustomerAddressResponseDto();
    }

    @Test
    void testCreateAddress() throws JsonProcessingException {
        MockMultipartFile file = new MockMultipartFile("file", new byte[0]);

        when(customerAddressService.createAddress(customerId, requestJson, file)).thenReturn(responseDto);

        ResponseEntity<CustomerAddressResponseDto> response = controller.createAddress(customerId, requestJson, file);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(customerAddressService, times(1)).createAddress(customerId, requestJson, file);
    }

    @Test
    void testUpdateAddress() throws JsonProcessingException {
        MockMultipartFile file = new MockMultipartFile("file", new byte[0]);

        when(customerAddressService.updateAddress(customerId, addressId, requestJson, file)).thenReturn(responseDto);

        ResponseEntity<CustomerAddressResponseDto> response = controller.updateAddress(customerId, addressId, requestJson, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(customerAddressService, times(1)).updateAddress(customerId, addressId, requestJson, file);
    }

    @Test
    void testGetActiveAddresses() {
        when(customerAddressService.getActiveAddressesByCustomerIdentity(customerId)).thenReturn(responseDto);

        ResponseEntity<CustomerAddressResponseDto> response = controller.getActiveAddresses(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(customerAddressService, times(1)).getActiveAddressesByCustomerIdentity(customerId);
    }

    @Test
    void testDeleteAddress() {
        doNothing().when(customerAddressService).deleteAddress(customerId, addressId);

        ResponseEntity<Void> response = controller.deleteAddress(customerId, addressId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerAddressService, times(1)).deleteAddress(customerId, addressId);
    }
}

