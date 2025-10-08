package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Branches;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerSearchServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LeadRepository leadRepository;

    @InjectMocks
    private CustomerSearchService customerSearchService;

    private Customer customer;
    private Lead lead;
    private UUID customerIdentity;

    @BeforeEach
    void setUp() {
        customerIdentity = UUID.randomUUID();

        Branches branch = new Branches();
        branch.setBranchCode("BR001");

        customer = new Customer();
        customer.setIdentity(customerIdentity);
        customer.setCustomerCode("CUST001");
        customer.setFirstName("John");
        customer.setMiddleName("Middle");
        customer.setLastName("Doe");
        customer.setDisplayName("John Doe");
        customer.setMobileNumber("9876543210");
        customer.setBranchId(branch);

        lead = new Lead();
        lead.setLeadCode("LEAD001");
        lead.setFullName("John Doe");
        lead.setContactNumber("9876543210");
    }

    @Test
    void testSearchCustomers_WithCustomersFound() {
        CustomerSearchRequestDto searchRequest = createSearchRequest();
        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), eq("9876543210"), eq("test@example.com"),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), eq("John Doe")
        )).thenReturn(Arrays.asList(customer));

        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());

        CustomerSearchResponseDto response = result.get(0);
        assertEquals(customerIdentity, response.getCustomerIdentity());
        assertTrue(response.getIsCustomerExist());
        assertEquals("CUST001", response.getCustomerCode());
        assertEquals("John", response.getFirstName());
        assertEquals("Middle", response.getMiddleName());
        assertEquals("Doe", response.getLastName());
        assertEquals("John Doe", response.getDisplayName());
        assertEquals("9876543210", response.getMobile());
        assertEquals("BR001", response.getBranchCode());

        verify(customerRepository).searchCustomersFlexible(
                "BR001", 1, "9876543210", "test@example.com",
                "ABCDE1234F", "123456789012", "VOTER12345",
                "A1234567", "John Doe"
        );
        verify(leadRepository, never()).searchLeadDetails(any(), any(), any());
    }

    @Test
    void testSearchCustomers_WithNullMobileNumber() {
        CustomerSearchRequestDto requestWithNullMobile = CustomerSearchRequestDto.builder()
                .branchCode("BR001")
                .branchId(1)
                .mobileNumber(null)
                .emailId("test@example.com")
                .panCard("ABCDE1234F")
                .aadhaarNumber("123456789012")
                .voterId("VOTER12345")
                .passportNumber("A1234567")
                .customerName("John Doe")
                .build();

        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), isNull(), eq("test@example.com"),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), eq("John Doe")
        )).thenReturn(Arrays.asList(customer));


        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(requestWithNullMobile);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerRepository).searchCustomersFlexible(
                "BR001", 1, null, "test@example.com",
                "ABCDE1234F", "123456789012", "VOTER12345",
                "A1234567", "John Doe"
        );
    }




    @Test
    void testSearchCustomers_NoCustomersFound_SearchInLeads() {
        CustomerSearchRequestDto searchRequest = createSearchRequest();
        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), eq("9876543210"), eq("test@example.com"),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), eq("John Doe")
        )).thenReturn(Collections.emptyList());

        lenient().when(leadRepository.searchLeadDetails("9876543210", "test@example.com", "John Doe"))
                .thenReturn(Arrays.asList(lead));

        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(searchRequest);

        assertNotNull(result);
        assertEquals(1, result.size());

        CustomerSearchResponseDto response = result.get(0);
        assertTrue(response.getIsLeadExist());
        assertEquals("LEAD001", response.getCustomerCode());
        assertEquals("John Doe", response.getFirstName());
        assertEquals("9876543210", response.getMobile());
        assertNull(response.getCustomerIdentity());
        assertNull(response.getIsCustomerExist());

        verify(customerRepository).searchCustomersFlexible(
                "BR001", 1, "9876543210", "test@example.com",
                "ABCDE1234F", "123456789012", "VOTER12345",
                "A1234567", "John Doe"
        );
        verify(leadRepository).searchLeadDetails("9876543210", "test@example.com", "John Doe");
    }

    @Test
    void testSearchCustomers_NoCustomersFound_NoLeadsFound() {
        CustomerSearchRequestDto searchRequest = createSearchRequest();
        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), eq("9876543210"), eq("test@example.com"),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), eq("John Doe")
        )).thenReturn(Collections.emptyList());

        lenient().when(leadRepository.searchLeadDetails("9876543210", "test@example.com", "John Doe"))
                .thenReturn(Collections.emptyList());

        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(searchRequest);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(customerRepository).searchCustomersFlexible(
                "BR001", 1, "9876543210", "test@example.com",
                "ABCDE1234F", "123456789012", "VOTER12345",
                "A1234567", "John Doe"
        );
        verify(leadRepository).searchLeadDetails("9876543210", "test@example.com", "John Doe");
    }

    @Test
    void testSearchCustomers_WithEmptyEmailInLeadSearch() {
        CustomerSearchRequestDto requestWithEmptyEmail = CustomerSearchRequestDto.builder()
                .branchCode("BR001")
                .branchId(1)
                .mobileNumber("9876543210")
                .emailId("")
                .panCard("ABCDE1234F")
                .aadhaarNumber("123456789012")
                .voterId("VOTER12345")
                .passportNumber("A1234567")
                .customerName("John Doe")
                .build();

        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), eq("9876543210"), isNull(),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), eq("John Doe")
        )).thenReturn(Collections.emptyList());

        lenient().when(leadRepository.searchLeadDetails(eq("9876543210"), isNull(), eq("John Doe")))
                .thenReturn(Arrays.asList(lead));

        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(requestWithEmptyEmail);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leadRepository).searchLeadDetails("9876543210", null, "John Doe");
    }

    @Test
    void testSearchCustomers_WithEmptyNameInLeadSearch() {
        CustomerSearchRequestDto requestWithEmptyName = CustomerSearchRequestDto.builder()
                .branchCode("BR001")
                .branchId(1)
                .mobileNumber("9876543210")
                .emailId("test@example.com")
                .panCard("ABCDE1234F")
                .aadhaarNumber("123456789012")
                .voterId("VOTER12345")
                .passportNumber("A1234567")
                .customerName("")
                .build();

        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), eq("9876543210"), eq("test@example.com"),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), isNull()
        )).thenReturn(Collections.emptyList());

        lenient().when(leadRepository.searchLeadDetails(eq("9876543210"), eq("test@example.com"), isNull()))
                .thenReturn(Arrays.asList(lead));

        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(requestWithEmptyName);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(leadRepository).searchLeadDetails("9876543210", "test@example.com", null);
    }

    @Test
    void testSearchCustomers_MultipleCustomersFound() {
        CustomerSearchRequestDto searchRequest = createSearchRequest();
        Customer customer2 = new Customer();
        customer2.setIdentity(UUID.randomUUID());
        customer2.setCustomerCode("CUST002");
        customer2.setFirstName("Jane");
        customer2.setLastName("Smith");
        customer2.setDisplayName("Jane Smith");
        customer2.setMobileNumber("9876543211");

        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), eq("9876543210"), eq("test@example.com"),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), eq("John Doe")
        )).thenReturn(Arrays.asList(customer, customer2));

        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(searchRequest);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("CUST001", result.get(0).getCustomerCode());
        assertEquals("CUST002", result.get(1).getCustomerCode());
    }

    @Test
    void testSearchCustomers_MultipleLeadsFound() {
        CustomerSearchRequestDto searchRequest = createSearchRequest();
        Lead lead2 = new Lead();
        lead2.setLeadCode("LEAD002");
        lead2.setFullName("Jane Smith");
        lead2.setContactNumber("9876543211");

        lenient().when(customerRepository.searchCustomersFlexible(
                eq("BR001"), eq(1), eq("9876543210"), eq("test@example.com"),
                eq("ABCDE1234F"), eq("123456789012"), eq("VOTER12345"),
                eq("A1234567"), eq("John Doe")
        )).thenReturn(Collections.emptyList());

        lenient().when(leadRepository.searchLeadDetails("9876543210", "test@example.com", "John Doe"))
                .thenReturn(Arrays.asList(lead, lead2));

        List<CustomerSearchResponseDto> result = customerSearchService.searchCustomers(searchRequest);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("LEAD001", result.get(0).getCustomerCode());
        assertEquals("LEAD002", result.get(1).getCustomerCode());
    }

    @Test
    void testMapToSearchResponseDto_WithNullBranch() {
        customer.setBranchId(null);

        CustomerSearchResponseDto result = customerSearchService.mapToSearchResponseDto(customer);

        assertNotNull(result);
        assertNull(result.getBranchCode());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void testMapLeadToSearchResponseDto() {
        CustomerSearchResponseDto result = customerSearchService.mapLeadToSearchResponseDto(lead);

        assertNotNull(result);
        assertTrue(result.getIsLeadExist());
        assertEquals("LEAD001", result.getCustomerCode());
        assertEquals("John Doe", result.getFirstName());
        assertEquals("9876543210", result.getMobile());
        assertNull(result.getCustomerIdentity());
        assertNull(result.getIsCustomerExist());
        assertNull(result.getMiddleName());
        assertNull(result.getLastName());
        assertNull(result.getDisplayName());
        assertNull(result.getBranchCode());
    }

    private CustomerSearchRequestDto createSearchRequest() {
        return CustomerSearchRequestDto.builder()
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
    }
}