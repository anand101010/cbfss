package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerForm60;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60RequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerForm60ResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerForm60Mapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerForm60Repository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.repository.DocumentMasterRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerForm60ServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerForm60Repository customerForm60Repository;
    @Mock
    private CustomerForm60Mapper form60Mapper;
    @Mock
    private DocumentMasterRepository documentRepository;
    @Mock
    private CustomerForm60Service service;

    private UUID customerIdentity;
    private Integer form60Id;
    private Customer customer;
    private CustomerForm60 form60;

    @Before
    public void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerForm60Repository = mock(CustomerForm60Repository.class);
        form60Mapper = mock(CustomerForm60Mapper.class);
        documentRepository = mock(DocumentMasterRepository.class);

        service = new CustomerForm60Service(
                customerForm60Repository,
                form60Mapper,
                customerRepository,
                documentRepository,
                customerForm60Repository
        );

        customerIdentity = UUID.fromString("00000000-0000-0000-0000-000000000001");
        form60Id = 1;

        customer = new Customer();
        customer.setCustomerId(100);
        customer.setIdentity(customerIdentity);

        form60 = new CustomerForm60();
        form60.setForm60Id(form60Id);
        form60.setCustomerId(customer);

    }

    @Test
    public void testGetForm60ById_Success() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByForm60IdAndCustomerId(form60Id, customer.getCustomerId()))
                .thenReturn(Optional.of(form60));

        CustomerForm60ResponseDto responseDto = new CustomerForm60ResponseDto();
        when(form60Mapper.toResponseDto(form60)).thenReturn(responseDto);

        CustomerForm60ResponseDto result = service.getForm60ById(customerIdentity, form60Id);

        assertNotNull(result);
    }

    @Test
    public void testGetForm60ById_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        try {
            service.getForm60ById(customerIdentity, form60Id);
            fail("Expected BusinessException for customer not found");
        } catch (BusinessException e) {
            assertEquals(ErrorCodes.NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void testGetForm60ById_Form60NotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByForm60IdAndCustomerId(form60Id, customer.getCustomerId()))
                .thenReturn(Optional.empty());

        try {
            service.getForm60ById(customerIdentity, form60Id);
            fail("Expected BusinessException for Form60 not found");
        } catch (BusinessException e) {
            assertEquals(ErrorCodes.NOT_FOUND, e.getErrorCode());
            assertTrue(e.getMessage().contains("Form 60 not found"));
        }
    }

    @Test
    public void testSaveForm60_Success() {

        CustomerForm60RequestDto requestDto = new CustomerForm60RequestDto();
        requestDto.setTransactionAmount(BigDecimal.valueOf(100000));
        requestDto.setCreatedBy(1);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(form60Mapper.toEntity(requestDto, customer, null, null)).thenReturn(form60);
        when(customerForm60Repository.save(form60)).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        verify(customerForm60Repository).save(form60);
    }

    @Test
    public void testSaveForm60_CustomerNotFound() {
        CustomerForm60RequestDto requestDto = new CustomerForm60RequestDto();
        requestDto.setTransactionAmount(BigDecimal.valueOf(100000));
        requestDto.setCreatedBy(1);
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        try {
            service.saveForm60(requestDto, customerIdentity);
            fail("Expected BusinessException for customer not found");
        } catch (BusinessException e) {
            assertEquals("Customer not found", e.getMessage());
            assertEquals(ErrorCodes.NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void testSaveForm60_ValidationFailure() {
        CustomerForm60RequestDto requestDto = new CustomerForm60RequestDto();
        requestDto.setTransactionAmount(BigDecimal.valueOf(100000));
        requestDto.setCreatedBy(1);
        requestDto.setTransactionAmount(BigDecimal.valueOf(0));
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));

        try {
            service.saveForm60(requestDto, customerIdentity);
            fail("Expected BusinessException for validation failure");
        } catch (BusinessException e) {
            assertEquals(ErrorCodes.VALIDATION_FAILED, e.getErrorCode());
            assertTrue(e.getMessage().contains("Transaction amount must be greater than zero"));
        }
    }


    @Test
    public void testUpdateForm60_Success() {
        CustomerForm60RequestDto requestDto = new CustomerForm60RequestDto();
        requestDto.setTransactionAmount(BigDecimal.valueOf(100000));
        requestDto.setCreatedBy(1);
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByForm60IdAndCustomerId(form60Id, customer.getCustomerId()))
                .thenReturn(Optional.of(form60));
        when(customerForm60Repository.save(form60)).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        verify(customerForm60Repository).save(form60);
    }

    @Test
    public void testUpdateForm60_CustomerNotFound() {

        CustomerForm60RequestDto requestDto = new CustomerForm60RequestDto();
        requestDto.setTransactionAmount(BigDecimal.valueOf(100000));
        requestDto.setCreatedBy(1);
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        try {
            service.updateForm60(customerIdentity, form60Id, requestDto);
            fail("Expected BusinessException for customer not found");
        } catch (BusinessException e) {
            assertEquals(ErrorCodes.NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void testUpdateForm60_Form60NotFound() {

        CustomerForm60RequestDto requestDto = new CustomerForm60RequestDto();
        requestDto.setTransactionAmount(BigDecimal.valueOf(100000));
        requestDto.setCreatedBy(1);
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByForm60IdAndCustomerId(form60Id, customer.getCustomerId()))
                .thenReturn(Optional.empty());

        try {
            service.updateForm60(customerIdentity, form60Id, requestDto);
            fail("Expected BusinessException for Form60 not found");
        } catch (BusinessException e) {
            assertEquals(ErrorCodes.NOT_FOUND, e.getErrorCode());
            assertTrue(e.getMessage().contains("Form 60 not found"));
        }
    }
}
