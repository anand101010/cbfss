package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAddressMapper;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerForm60Mapper;
import com.incede.nbfc.core.monolith.customer.mapper.JasperForm60Mapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.enums.AddressTypes;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.BranchesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.DocumentMasterRepository;
import com.incede.nbfc.core.monolith.report.OutputFormat;
import com.incede.nbfc.core.monolith.report.ReportGenerator;
import com.incede.nbfc.core.monolith.report.ReportName;
import com.incede.nbfc.core.monolith.service.VaultService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
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
    private ReportGenerator reportGenerator;
    @Mock
    private JasperForm60Mapper jasperForm60Mapper;
    @Mock
    private CustomerAddressRepository customerAddressRepository;
    @Mock
    private AddressTypeRepository addressTypeRepository;
    @Mock
    private CustomerAddressService customerAddressService;
    @Mock
    private CustomerProfileExtraRepository customerProfileExtraRepository;
    @Mock
    private CustomerEmploymentRepository customerEmploymentRepository;
    @Mock
    private CustomerAddressMapper customerAddressMapper;
    @Mock
    private BranchesRepository branchesRepository;
    @Mock
    private VaultService vaultService;

    @InjectMocks
    private CustomerForm60Service service;

    private UUID customerIdentity;
    private UUID form60Identity;
    private UUID branchIdentity;
    private UUID pidDocIdentity;
    private UUID addDocIdentity;
    private Customer customer;
    private CustomerForm60 form60;
    private Branches branch;
    private DocumentMaster pidDoc;
    private DocumentMaster addDoc;
    private CustomerForm60RequestDto requestDto;
    private CustomerForm60ResponseDto responseDto;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        customerIdentity = UUID.randomUUID();
        form60Identity = UUID.randomUUID();
        branchIdentity = UUID.randomUUID();
        pidDocIdentity = UUID.randomUUID();
        addDocIdentity = UUID.randomUUID();

        customer = new Customer();
        customer.setCustomerId(100);
        customer.setIdentity(customerIdentity);
        customer.setIsDel(false);

        form60 = new CustomerForm60();
        form60.setForm60Id(1);
        form60.setCustomerId(customer);
        form60.setIdentity(form60Identity);
        form60.setDocRefId("DOC123");
        form60.setFilePath("/path/to/file");
        form60.setIsDel(false);

        branch = new Branches();
        branch.setBranchId(1);
        branch.setIdentity(branchIdentity);
        branch.setPlaceName("Kakkanad");

        pidDoc = new DocumentMaster();
        pidDoc.setIdentity(pidDocIdentity);

        addDoc = new DocumentMaster();
        addDoc.setIdentity(addDocIdentity);

        requestDto = new CustomerForm60RequestDto();
        requestDto.setCustomerId(customerIdentity);
        requestDto.setBranchId(branchIdentity);
        requestDto.setTransactionAmount(BigDecimal.valueOf(200000));
        requestDto.setTransactionDate(LocalDate.of(2023, 5, 25));
        requestDto.setModeOfTransaction("CASH");
        requestDto.setCreatedBy(1);
        requestDto.setFormFileId(1);
        requestDto.setDocRefId("DOC123");
        requestDto.setFilePath("/path/to/file");
        requestDto.setPidDocumentId(pidDocIdentity);
        requestDto.setAddDocumentId(addDocIdentity);

        responseDto = new CustomerForm60ResponseDto();
        responseDto.setIdentity(form60Identity);
        responseDto.setBranchId(1);
        responseDto.setTransactionAmount(BigDecimal.valueOf(200000));
        responseDto.setTransactionDate(LocalDate.of(2023, 5, 25));
        responseDto.setModeOfTransaction("CASH");
        responseDto.setFormFileId(1);
        responseDto.setDocRefId("DOC123");
        responseDto.setFilePath("/path/to/file");
    }


    @Test
    public void testSaveForm60_Success() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(branchIdentity)).thenReturn(Optional.of(branch));
        when(documentRepository.findByIdentity(pidDocIdentity)).thenReturn(Optional.of(pidDoc));
        when(documentRepository.findByIdentity(addDocIdentity)).thenReturn(Optional.of(addDoc));
        when(form60Mapper.toEntity(any(), any(), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(responseDto);

        CustomerForm60ResponseDto result = service.saveForm60(requestDto, customerIdentity);

        assertNotNull(result);
        verify(customerForm60Repository).save(any());
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());
        service.saveForm60(requestDto, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_AmountMissing_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);
        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_DateMissing_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(100));
        request.setCreatedBy(1);
        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_NegativeAmount_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(-1));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);
        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_AboveMaxAmount_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(new BigDecimal("600000"));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);
        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_MissingCreatedBy_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(100));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(null);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));

        service.saveForm60(request, customerIdentity);
    }

    @Test
    public void testSaveForm60_WithMaskedAadhaar() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(2000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());
        request.setMaskedAdhar("123412341234");

        FinaVaultResponseDto vaultResponse = new FinaVaultResponseDto("123", "ABC", "UID123", null, null, null, null, null);
        vaultResponse.setStatus("S");

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);
        when(vaultService.generateVaultIdAndMaskAadhaar("123412341234")).thenReturn(vaultResponse);
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto response = service.saveForm60(request, customerIdentity);

        assertNotNull(response);
        verify(vaultService).generateVaultIdAndMaskAadhaar("123412341234");
    }

    @Test
    public void testSaveForm60_WithAlreadyMaskedAadhaar() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(2000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());
        request.setMaskedAdhar("XXXX-XXXX-1234");

        FinaVaultResponseDto vaultResponse = new FinaVaultResponseDto("123", "ABC", "UID123", null, null, null, null, null);
        vaultResponse.setStatus("S");

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);
        when(vaultService.generateVaultIdAndMaskAadhaar("XXXX-XXXX-1234")).thenReturn(vaultResponse);
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto response = service.saveForm60(request, customerIdentity);

        assertNotNull(response);
        verify(vaultService).generateVaultIdAndMaskAadhaar("XXXX-XXXX-1234");
        verify(customerForm60Repository).save(any());
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_BranchNotFound_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(100));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);
        request.setBranchId(UUID.randomUUID());

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.empty());

        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_DataIntegrityViolation_ShouldThrow() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(branchIdentity)).thenReturn(Optional.of(branch));
        when(documentRepository.findByIdentity(pidDocIdentity)).thenReturn(Optional.of(pidDoc));
        when(documentRepository.findByIdentity(addDocIdentity)).thenReturn(Optional.of(addDoc));
        when(form60Mapper.toEntity(any(), any(), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(any())).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        service.saveForm60(requestDto, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_AadhaarMaskingFailed_NullResponse_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(2000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());
        request.setMaskedAdhar("123412341234");

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);
        when(vaultService.generateVaultIdAndMaskAadhaar("123412341234")).thenReturn(null);

        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_AadhaarMaskingFailed_StatusN_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(2000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());
        request.setMaskedAdhar("123412341234");

        FinaVaultResponseDto vaultResponse = new FinaVaultResponseDto("123", "ABC", "UID123", null, null, null, null, null);
        vaultResponse.setStatus("N");
        vaultResponse.setErrorCode("INVALID_AADHAAR");

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);
        when(vaultService.generateVaultIdAndMaskAadhaar("123412341234")).thenReturn(vaultResponse);

        service.saveForm60(request, customerIdentity);
    }

    // ---------------- updateForm60 ----------------

    @Test
    public void testUpdateForm60_Success() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(5000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        doNothing().when(form60Mapper).updateEntityFromDto(form60, request, null, null);
        when(customerForm60Repository.save(form60)).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto response = service.updateForm60(customerIdentity, form60Identity, request);

        assertNotNull(response);
        verify(customerForm60Repository).save(form60);
    }

    @Test(expected = BusinessException.class)
    public void testUpdateForm60_FormNotFound_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(100));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());

        service.updateForm60(customerIdentity, form60Identity, request);
    }

    @Test(expected = BusinessException.class)
    public void testUpdateForm60_InvalidAmount_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(-50));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));

        service.updateForm60(customerIdentity, form60Identity, request);
    }

    @Test
    public void testUpdateForm60_MissingCreatedBy_Success() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(500));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(null);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        doNothing().when(form60Mapper).updateEntityFromDto(form60, request, null, null);
        when(customerForm60Repository.save(form60)).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto response = service.updateForm60(customerIdentity, form60Identity, request);

        assertNotNull(response);
        verify(customerForm60Repository).save(form60);
    }

    @Test(expected = BusinessException.class)
    public void testUpdateForm60_DataIntegrityViolation_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(500));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        doNothing().when(form60Mapper).updateEntityFromDto(form60, request, null, null);
        when(customerForm60Repository.save(form60)).thenThrow(new DataIntegrityViolationException("Constraint violation"));

        service.updateForm60(customerIdentity, form60Identity, request);
    }

    @Test(expected = BusinessException.class)
    public void testUpdateForm60_AadhaarMaskingFailed_NullResponse_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(500));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());
        request.setMaskedAdhar("123412341234");

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        doNothing().when(form60Mapper).updateEntityFromDto(form60, request, null, null);
        when(vaultService.generateVaultIdAndMaskAadhaar("123412341234")).thenReturn(null);

        service.updateForm60(customerIdentity, form60Identity, request);
    }

    @Test(expected = BusinessException.class)
    public void testUpdateForm60_AadhaarMaskingFailed_StatusN_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(500));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());
        request.setMaskedAdhar("123412341234");

        FinaVaultResponseDto vaultResponse = new FinaVaultResponseDto("123", "ABC", "UID123", null, null, null, null, null);
        vaultResponse.setStatus("N");
        vaultResponse.setErrorCode("INVALID_AADHAAR");

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        doNothing().when(form60Mapper).updateEntityFromDto(form60, request, null, null);
        when(vaultService.generateVaultIdAndMaskAadhaar("123412341234")).thenReturn(vaultResponse);

        service.updateForm60(customerIdentity, form60Identity, request);
    }

    // ---------------- getForm60ById ----------------

    @Test
    public void testGetForm60ById_Success() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(responseDto);

        CustomerForm60ResponseDto result = service.getForm60ByIdentity(customerIdentity, form60Identity);
        assertNotNull(result);
        assertEquals(form60Identity, result.getIdentity());
    }

    @Test(expected = BusinessException.class)
    public void testGetForm60ById_FormNotFound_ShouldThrow() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());

        service.getForm60ByIdentity(customerIdentity, form60Identity);
    }

    @Test(expected = BusinessException.class)
    public void testGetForm60ById_CustomerNotFound_ShouldThrow() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());
        service.getForm60ByIdentity(customerIdentity, form60Identity);
    }


    @Test
    public void testGetForm60ById_FormNotBelongingToCustomer_Success() {
        Customer otherCustomer = new Customer();
        otherCustomer.setCustomerId(200);
        otherCustomer.setIdentity(UUID.randomUUID());
        form60.setCustomerId(otherCustomer);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto result = service.getForm60ByIdentity(customerIdentity, form60Identity);

        assertNotNull(result);
        verify(customerRepository).findByIdentity(customerIdentity);
        verify(customerForm60Repository).findByIdentity(form60Identity);
        verify(form60Mapper).toResponseDto(form60);
    }

    @Test
    public void testGetCustomerDesignation_Success() {
        CustomerEmployment employment = new CustomerEmployment();
        Designations designation = new Designations();
        designation.setDesignationId(1); // Assuming this sets an Integer
        designation.setCode("CODE");
        designation.setName("NAME");
        employment.setDesignationId(designation);

        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));

        CustomerDesignationResponseDto result = service.getCustomerDesignation(customer);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getDesignationId()); // Explicitly use Integer
        assertEquals("CODE", result.getCode());
        assertEquals("NAME", result.getName());
    }
    @Test(expected = RuntimeException.class)
    public void testGetCustomerDesignation_NotFound_ShouldThrow() {
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        service.getCustomerDesignation(customer);
    }

    @Test(expected = RuntimeException.class)
    public void testGetCustomerDesignation_NoDesignation_ShouldThrow() {
        CustomerEmployment employment = new CustomerEmployment();
        employment.setDesignationId(null);
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));
        service.getCustomerDesignation(customer);
    }

    // ---------------- getPurpose ----------------

    @Test
    public void testGetCustomerPurpose_Success() {
        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        Purpose purpose = new Purpose();
        purpose.setPurposeId(1); // Assuming this sets an Integer
        purpose.setCode("CODE");
        purpose.setName("NAME");
        profileExtra.setPurposeId(purpose);

        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));

        CustomerPurposeResponseDto result = service.getCustomerPurpose(customer);

        assertNotNull(result);
        assertEquals(Integer.valueOf(1), result.getPurposeId()); // Explicitly use Integer
        assertEquals("CODE", result.getCode());
        assertEquals("NAME", result.getName());
    }
    @Test(expected = RuntimeException.class)
    public void testGetCustomerPurpose_NotFound_ShouldThrow() {
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        service.getCustomerPurpose(customer);
    }

    @Test(expected = RuntimeException.class)
    public void testGetCustomerPurpose_PurposeNull_ShouldThrow() {
        CustomerProfileExtra extra = new CustomerProfileExtra();
        extra.setPurposeId(null);
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(extra));
        service.getCustomerPurpose(customer);
    }

    // ---------------- getPermanentAddress ----------------

    @Test
    public void testGetPermanentAddress_Success() {
        AddressType addressType = new AddressType();
        CustomerAddress customerAddress = new CustomerAddress();
        CustomerAddressDetailDto addressDto = new CustomerAddressDetailDto();

        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.of(addressType));
        when(customerAddressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(any(), any()))
                .thenReturn(Collections.singletonList(customerAddress));
        when(customerAddressMapper.mapToCustomerAddressDetailDto(any(), any())).thenReturn(addressDto);

        CustomerAddressDetailDto result = service.getPermanentAddress(customer);

        assertNotNull(result);
        verify(customerAddressMapper).mapToCustomerAddressDetailDto(any(), any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetPermanentAddress_AddressTypeNotFound() {
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.empty());
        service.getPermanentAddress(customer);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetPermanentAddress_NoAddressFound() {
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.of(new AddressType()));
        when(customerAddressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(any(), any()))
                .thenReturn(Collections.emptyList());
        service.getPermanentAddress(customer);
    }

    // ---------------- getBranchPlace ----------------

    @Test
    public void testGetBranchPlaceByForm60Identity_Success() {
        form60.setBranchId(branch);

        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));

        String result = service.getBranchPlaceByForm60Identity(form60Identity);
        assertNotNull(result);
        assertEquals("Kakkanad", result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetBranchPlaceByForm60Identity_NotFound() {
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());
        service.getBranchPlaceByForm60Identity(form60Identity);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetBranchPlaceByForm60Identity_NullBranch_ShouldThrow() {
        form60.setBranchId(null);

        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));

        service.getBranchPlaceByForm60Identity(form60Identity);
    }

    // ---------------- uploadSignedForm60 ----------------

    @Test
    public void testUploadSignedForm60_Success() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        when(customerForm60Repository.save(form60)).thenReturn(form60);

        Form60UploadResponseDto response = service.uploadSignedForm60(customerIdentity, form60Identity);

        assertNotNull(response);
        assertEquals(form60Identity, response.getForm60Identity());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUploadSignedForm60_CustomerNotFound_ShouldThrow() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.empty());
        service.uploadSignedForm60(customerIdentity, form60Identity);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUploadSignedForm60_Form60NotFound_ShouldThrow() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());
        service.uploadSignedForm60(customerIdentity, form60Identity);
    }

    // ---------------- generateForm60PreviewPdf ----------------

    @Test
    public void testGenerateForm60PreviewPdf_Success() throws Exception {
        form60.setBranchId(branch);
        CustomerForm60ResponseDto dto = new CustomerForm60ResponseDto();
        dto.setBranchId(1);
        dto.setIdentity(form60Identity);

        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(dto);
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.of(new AddressType()));
        when(customerAddressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(any(), any()))
                .thenReturn(Collections.singletonList(new CustomerAddress()));
        when(customerAddressMapper.mapToCustomerAddressDetailDto(any(), any())).thenReturn(new CustomerAddressDetailDto());

        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        profileExtra.setCustomer(customer);
        Purpose purpose = new Purpose();
        purpose.setPurposeId(1);
        purpose.setCode("CODE");
        purpose.setName("NAME");
        profileExtra.setPurposeId(purpose);
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));

        CustomerEmployment employment = new CustomerEmployment();
        Designations designation = new Designations();
        designation.setDesignationId(1);
        designation.setCode("CODE");
        designation.setName("NAME");
        employment.setDesignationId(designation);
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));

        when(jasperForm60Mapper.form60ToJasperDto(any(), any(), any(), any(), any(), any())).thenReturn(new HashMap<>());
        when(reportGenerator.generate(eq(ReportName.FORM60), any(), any(), eq(OutputFormat.PDF)))
                .thenReturn(new byte[]{1, 2, 3});

        byte[] result = service.generateForm60PreviewPdf(customerIdentity, form60Identity);

        assertNotNull(result);
        assertEquals(3, result.length);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGenerateForm60PreviewPdf_CustomerNotFound() throws Exception {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.empty());
        service.generateForm60PreviewPdf(customerIdentity, form60Identity);
    }

    @Test(expected = BusinessException.class)
    public void testGenerateForm60PreviewPdf_FormNotFound() throws Exception {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());

        service.generateForm60PreviewPdf(customerIdentity, form60Identity);
    }

    @Test(expected = BusinessException.class)
    public void testGenerateForm60PreviewPdf_ReportGeneratorFails_ShouldThrow() throws Exception {
        form60.setBranchId(branch);
        CustomerForm60ResponseDto dto = new CustomerForm60ResponseDto();
        dto.setBranchId(1);
        dto.setIdentity(form60Identity);

        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(dto);
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.of(new AddressType()));
        when(customerAddressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(any(), any()))
                .thenReturn(Collections.singletonList(new CustomerAddress()));
        when(customerAddressMapper.mapToCustomerAddressDetailDto(any(), any())).thenReturn(new CustomerAddressDetailDto());

        CustomerProfileExtra profileExtra = new CustomerProfileExtra();
        profileExtra.setCustomer(customer);
        Purpose purpose = new Purpose();
        purpose.setPurposeId(1);
        purpose.setCode("CODE");
        purpose.setName("NAME");
        profileExtra.setPurposeId(purpose);
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(profileExtra));

        CustomerEmployment employment = new CustomerEmployment();
        Designations designation = new Designations();
        designation.setDesignationId(1);
        designation.setCode("CODE");
        designation.setName("NAME");
        employment.setDesignationId(designation);
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));

        when(jasperForm60Mapper.form60ToJasperDto(any(), any(), any(), any(), any(), any())).thenReturn(new HashMap<>());
        when(reportGenerator.generate(eq(ReportName.FORM60), any(), any(), eq(OutputFormat.PDF)))
                .thenThrow(new RuntimeException("report fail"));

        service.generateForm60PreviewPdf(customerIdentity, form60Identity);
    }
}