package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAddressMapper;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerForm60Mapper;
import com.incede.nbfc.core.monolith.customer.mapper.JasperForm60Mapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerForm60ServiceTest {

    private CustomerRepository customerRepository;
    private CustomerForm60Repository customerForm60Repository;
    private CustomerForm60Mapper form60Mapper;
    private DocumentMasterRepository documentRepository;
    private ReportGenerator reportGenerator;
    private JasperForm60Mapper jasperForm60Mapper;
    private CustomerAddressRepository customerAddressRepository;
    private AddressTypeRepository addressTypeRepository;
    private CustomerAddressService customerAddressService;
    private CustomerProfileExtraRepository customerProfileExtraRepository;
    private CustomerEmploymentRepository customerEmploymentRepository;
    private CustomerAddressMapper customerAddressMapper;
    private BranchesRepository branchesRepository;
    private VaultService vaultService;

    private CustomerForm60Service service;
    private UUID customerIdentity;
    private UUID form60Identity;
    private Customer customer;
    private CustomerForm60 form60;

    @Before
    public void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerForm60Repository = mock(CustomerForm60Repository.class);
        form60Mapper = mock(CustomerForm60Mapper.class);
        documentRepository = mock(DocumentMasterRepository.class);
        reportGenerator = mock(ReportGenerator.class);
        jasperForm60Mapper = mock(JasperForm60Mapper.class);
        customerAddressRepository = mock(CustomerAddressRepository.class);
        addressTypeRepository = mock(AddressTypeRepository.class);
        customerAddressService = mock(CustomerAddressService.class);
        customerProfileExtraRepository = mock(CustomerProfileExtraRepository.class);
        customerEmploymentRepository = mock(CustomerEmploymentRepository.class);
        customerAddressMapper = mock(CustomerAddressMapper.class);
        branchesRepository = mock(BranchesRepository.class);
        vaultService = mock(VaultService.class);

        service = new CustomerForm60Service(
                customerForm60Repository,
                form60Mapper,
                customerRepository,
                documentRepository,
                reportGenerator,
                jasperForm60Mapper,
                customerAddressRepository,
                addressTypeRepository,
                customerAddressService,
                customerProfileExtraRepository,
                customerEmploymentRepository,
                customerAddressMapper,
                branchesRepository,
                vaultService
        );

        customerIdentity = UUID.randomUUID();
        form60Identity = UUID.randomUUID();
        customer = new Customer();
        customer.setCustomerId(100);
        customer.setIdentity(customerIdentity);

        form60 = new CustomerForm60();
        form60.setForm60Id(1);
        form60.setCustomerId(customer);
        form60.setIdentity(form60Identity);
    }

    // ---------------- saveForm60 ----------------
    @Test
    public void testSaveForm60_Success() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(1000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(request.getBranchId())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(form60)).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto response = service.saveForm60(request, customerIdentity);

        assertNotNull(response);
        verify(customerForm60Repository).save(form60);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_CustomerNotFound() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.TEN);
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());
        service.saveForm60(request, customerIdentity);
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
        request.setTransactionAmount(BigDecimal.TEN);
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

    // Aadhaar vault masking
    @Test
    public void testSaveForm60_WithMaskedAadhaar() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(2000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());
        request.setMaskedAdhar("123412341234");

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);

        when(vaultService.generateVaultIdAndMaskAadhaar("123412341234"))
                .thenReturn(new FinaVaultResponseDto("123", "ABC", "UID123", null, null, null, null, null));

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
        request.setMaskedAdhar("XXXX-XXXX-1234"); // already masked

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto response = service.saveForm60(request, customerIdentity);

        assertNotNull(response);
        verify(vaultService, never()).generateVaultIdAndMaskAadhaar(anyString());
    }

    // ---------------- updateForm60 ----------------
    @Test
    public void testUpdateForm60_Success() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(5000));
        request.setCreatedBy(1);
        request.setTransactionDate(LocalDate.now());

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60.getIdentity())).thenReturn(Optional.of(form60));
        when(customerForm60Repository.save(form60)).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto response = service.updateForm60(customerIdentity, form60.getIdentity(), request);

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

    // ---------------- getForm60ById ----------------
    @Test
    public void testGetForm60ById_Success() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60.getIdentity())).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto result = service.getForm60ByIdentity(customerIdentity, form60.getIdentity());
        assertNotNull(result);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetForm60ById_FormNotFound_ShouldThrow() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60.getIdentity())).thenReturn(Optional.empty());

        service.getForm60ByIdentity(customerIdentity, form60.getIdentity());
    }

    // ---------------- getDesignation ----------------
    @Test(expected = RuntimeException.class)
    public void testGetCustomerDesignation_NotFound_ShouldThrow() {
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        service.getCustomerDesignation(customer);
    }

    // ---------------- getPurpose ----------------
    @Test(expected = RuntimeException.class)
    public void testGetCustomerPurpose_NotFound_ShouldThrow() {
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        service.getCustomerPurpose(customer);
    }

    // ---------------- getPermanentAddress ----------------
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
    @Test(expected = ResourceNotFoundException.class)
    public void testGetBranchPlaceByForm60Identity_NotFound() {
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());
        service.getBranchPlaceByForm60Identity(form60Identity);
    }

    @Test
    public void testGetBranchPlaceByForm60Identity_Success() {
        Branches branch = new Branches();
        branch.setBranchId(10);
        form60.setBranchId(branch);

        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));

        String result = service.getBranchPlaceByForm60Identity(form60Identity);
        assertNotNull(result);
        assertEquals("Kakkanad", result);
    }

    // ---------------- uploadSignedForm60 ----------------
    @Test(expected = BusinessException.class)
    public void testUploadSignedForm60_EmptyFile_ShouldThrow() {
        MultipartFile file = new MockMultipartFile("file", new byte[0]);
        service.uploadSignedForm60(customerIdentity, form60Identity, file);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUploadSignedForm60_CustomerNotFound_ShouldThrow() {
        MultipartFile file = new MockMultipartFile("file", "f60.pdf", "application/pdf", "data".getBytes());
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.empty());
        service.uploadSignedForm60(customerIdentity, form60Identity, file);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testUploadSignedForm60_Form60NotFound_ShouldThrow() {
        MultipartFile file = new MockMultipartFile("file", "f60.pdf", "application/pdf", "data".getBytes());
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());
        service.uploadSignedForm60(customerIdentity, form60Identity, file);
    }

    @Test(expected = BusinessException.class)
    public void testUploadSignedForm60_InvalidExtension_ShouldThrow() {
        MultipartFile file = new MockMultipartFile("file", "f60.txt", "text/plain", "bad".getBytes());
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        service.uploadSignedForm60(customerIdentity, form60Identity, file);
    }

    @Test
    public void testUploadSignedForm60_Success() {
        MultipartFile file = new MockMultipartFile("file", "f60.pdf", "application/pdf", "data".getBytes());
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));

        Form60UploadResponseDto response = service.uploadSignedForm60(customerIdentity, form60Identity, file);

        assertNotNull(response);
        assertEquals(form60Identity, response.getForm60Identity());
    }

    // ---------------- generateForm60PreviewPdf ----------------
    @Test
    public void testGenerateForm60PreviewPdf_Success() throws Exception {
        CustomerForm60ResponseDto dto = new CustomerForm60ResponseDto();
        dto.setBranchId(1);

        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
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
        when(branchesRepository.findByBranchId(1)).thenReturn(Optional.of(new Branches()));

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

    @Test(expected = ResourceNotFoundException.class)
    public void testGenerateForm60PreviewPdf_FormNotFound() throws Exception {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.empty());
        service.generateForm60PreviewPdf(customerIdentity, form60Identity);
    }

    // updateForm60 validation failures
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

    @Test(expected = BusinessException.class)
    public void testUpdateForm60_MissingCreatedBy_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(500));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(null);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));

        service.updateForm60(customerIdentity, form60Identity, request);
    }

    // getForm60ByIdentity - customer not found
    @Test(expected = ResourceNotFoundException.class)
    public void testGetForm60ById_CustomerNotFound_ShouldThrow() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());
        service.getForm60ByIdentity(customerIdentity, form60.getIdentity());
    }

    // getBranchPlaceByForm60Identity - branch is null
    @Test(expected = ResourceNotFoundException.class)
    public void testGetBranchPlaceByForm60Identity_BranchNull_ShouldThrow() {
        form60.setBranchId(null);
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        service.getBranchPlaceByForm60Identity(form60Identity);
    }

    // uploadSignedForm60 - wrong content type but pdf extension
    @Test(expected = BusinessException.class)
    public void testUploadSignedForm60_WrongMimeType_ShouldThrow() {
        MultipartFile file = new MockMultipartFile("file", "f60.pdf", "text/plain", "data".getBytes());
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        service.uploadSignedForm60(customerIdentity, form60Identity, file);
    }

    // generateForm60PreviewPdf - permanent address type missing
    @Test(expected = ResourceNotFoundException.class)
    public void testGenerateForm60PreviewPdf_AddressTypeMissing_ShouldThrow() throws Exception {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.empty());

        service.generateForm60PreviewPdf(customerIdentity, form60Identity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_BranchNotFound_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(100));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.empty());

        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = BusinessException.class)
    public void testSaveForm60_DataIntegrityViolation_ShouldThrow() {
        CustomerForm60RequestDto request = new CustomerForm60RequestDto();
        request.setTransactionAmount(BigDecimal.valueOf(100));
        request.setTransactionDate(LocalDate.now());
        request.setCreatedBy(1);

        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        when(form60Mapper.toEntity(eq(request), eq(customer), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(any())).thenThrow(new org.springframework.dao.DataIntegrityViolationException("duplicate"));

        service.saveForm60(request, customerIdentity);
    }

    @Test(expected = RuntimeException.class)
    public void testGetCustomerDesignation_NoDesignation_ShouldThrow() {
        CustomerEmployment employment = new CustomerEmployment();
        employment.setDesignationId(null);
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));
        service.getCustomerDesignation(customer);
    }

    @Test(expected = RuntimeException.class)
    public void testGetCustomerPurpose_PurposeNull_ShouldThrow() {
        CustomerProfileExtra extra = new CustomerProfileExtra();
        extra.setPurposeId(null);
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(extra));
        service.getCustomerPurpose(customer);
    }

    @Test(expected = BusinessException.class)
    public void testGenerateForm60PreviewPdf_ReportGeneratorFails_ShouldThrow() throws Exception {
        CustomerForm60ResponseDto dto = new CustomerForm60ResponseDto();
        dto.setBranchId(1);

        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Identity)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(dto);
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.of(new AddressType()));
        when(customerAddressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(any(), any()))
                .thenReturn(Collections.singletonList(new CustomerAddress()));
        when(customerAddressMapper.mapToCustomerAddressDetailDto(any(), any())).thenReturn(new CustomerAddressDetailDto());
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(new CustomerProfileExtra()));
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(new CustomerEmployment()));
        when(branchesRepository.findByBranchId(1)).thenReturn(Optional.of(new Branches()));

        when(jasperForm60Mapper.form60ToJasperDto(any(), any(), any(), any(), any(), any())).thenReturn(new HashMap<>());
        when(reportGenerator.generate(eq(ReportName.FORM60), any(), any(), eq(OutputFormat.PDF)))
                .thenThrow(new RuntimeException("report fail"));

        service.generateForm60PreviewPdf(customerIdentity, form60Identity);
    }


}