package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerSearchControllerTest {

    @Mock
    private CustomerSearchService customerSearchService;

    @InjectMocks
    private CustomerSearchController customerSearchController;

    private CustomerSearchRequestDto searchRequest;
    private CustomerSearchResponseDto responseDto;
    private List<CustomerSearchResponseDto> responseList;

    @BeforeEach
    void setUp() {
        searchRequest = CustomerSearchRequestDto.builder()
                .branchCode("BR001")
                .branchId(1)
                .mobileNumber("9876543210")
                .emailId("test@example.com")
                .panCard("ABCDE1234F")
                .aadhaarNumber("123456789012")
                .voterId("VOTER12345")
                .passportNumber("A1234567")
                .customerName("John Doe")
                .build();

        responseDto = CustomerSearchResponseDto.builder()
                .customerIdentity(UUID.randomUUID())
                .isCustomerExist(true)
                .isLeadExist(false)
                .branchCode("BR001")
                .displayName("John Doe")
                .customerCode("CUST001")
                .firstName("John")
                .middleName("Middle")
                .lastName("Doe")
                .fatherName("Robert Doe")
                .houseName("Sunrise Apartments")
                .mobile("9876543210")
                .city("Mumbai")
                .build();

        responseList = Arrays.asList(responseDto);
    }

    @Test
    void testSearchCustomers_WithAllParameters_ReturnsOk() {

        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(responseList);


        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                "BR001", 1, "9876543210", "test@example.com", "ABCDE1234F",
                "123456789012", "VOTER12345", "A1234567", "John Doe"
        );


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(responseDto, response.getBody().get(0));

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }

    @Test
    void testSearchCustomers_WithNullParameters_ReturnsOk() {

        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(responseList);


        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                null, null, null, null, null, null, null, null, null
        );


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }

    @Test
    void testSearchCustomers_WithEmptyResults_ReturnsNoContent() {

        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(Collections.emptyList());


        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                "BR001", 1, "9876543210", "test@example.com", "ABCDE1234F",
                "123456789012", "VOTER12345", "A1234567", "John Doe"
        );


        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }

    @Test
    void testSearchCustomers_WithPartialParameters_ReturnsOk() {

        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(responseList);


        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                "BR001", null, "9876543210", null, null, null, null, null, "John"
        );


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }

    @Test
    void testSearchCustomers_WithOnlyBranchCode_ReturnsOk() {

        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(responseList);


        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                "BR001", null, null, null, null, null, null, null, null
        );


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }

    @Test
    void testSearchCustomers_WithOnlyMobileNumber_ReturnsOk() {

        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(responseList);

        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                null, null, "9876543210", null, null, null, null, null, null
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }

    @Test
    void testSearchCustomers_WithMultipleResults_ReturnsOk() {
        CustomerSearchResponseDto responseDto2 = CustomerSearchResponseDto.builder()
                .customerIdentity(UUID.randomUUID())
                .isCustomerExist(true)
                .customerCode("CUST002")
                .firstName("Jane")
                .lastName("Smith")
                .mobile("9876543211")
                .build();

        List<CustomerSearchResponseDto> multipleResults = Arrays.asList(responseDto, responseDto2);
        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(multipleResults);

        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                "BR001", 1, "9876543210", "test@example.com", "ABCDE1234F",
                "123456789012", "VOTER12345", "A1234567", "John Doe"
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }






    @Test
    void testSearchCustomers_WithEmptyStringParameters_ReturnsOk() {
        when(customerSearchService.searchCustomers(any(CustomerSearchRequestDto.class)))
                .thenReturn(responseList);

        ResponseEntity<List<CustomerSearchResponseDto>> response = customerSearchController.searchCustomers(
                "", null, "", "", "", "", "", "", ""
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());

        verify(customerSearchService).searchCustomers(any(CustomerSearchRequestDto.class));
    }


}