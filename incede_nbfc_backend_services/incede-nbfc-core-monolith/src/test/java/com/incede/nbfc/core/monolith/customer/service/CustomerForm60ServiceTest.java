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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerForm60ServiceTest {

    @Mock private CustomerForm60Repository customerForm60Repository;
    @Mock private CustomerForm60Mapper form60Mapper;
    @Mock private CustomerRepository customerRepository;
    @Mock private DocumentMasterRepository documentRepository;
    @Mock private ReportGenerator reportGenerator;
    @Mock private JasperForm60Mapper jasperForm60Mapper;
    @Mock private CustomerAddressRepository customerAddressRepository;
    @Mock private AddressTypeRepository addressTypeRepository;
    @Mock private CustomerAddressService customerAddressService;
    @Mock private CustomerProfileExtraRepository customerProfileExtraRepository;
    @Mock private CustomerEmploymentRepository customerEmploymentRepository;
    @Mock private CustomerAddressMapper customerAddressMapper;
    @Mock private BranchesRepository branchesRepository;
    @Mock private VaultService vaultService;

    @InjectMocks private CustomerForm60Service service;

    private UUID customerId;
    private UUID form60Id;
    private Customer customer;
    private CustomerForm60 form60;
    private CustomerForm60RequestDto requestDto;
    private DocumentMaster documentMaster; // ADDED: DocumentMaster field

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        form60Id = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("CUST-001");
        customer.setMobileNumber("9999999999");
        customer.setFirstName("John");
        customer.setLastName("Doe");

        form60 = new CustomerForm60();
        form60.setIdentity(form60Id);
        form60.setFilePath("test.pdf");

        documentMaster = new DocumentMaster();
        documentMaster.setIdentity(UUID.randomUUID());
        documentMaster.setDocname("PID ");

        requestDto = new CustomerForm60RequestDto();
        requestDto.setTransactionAmount(BigDecimal.valueOf(10000));
        requestDto.setTransactionDate(LocalDate.now());
        requestDto.setCreatedBy(1);

        requestDto.setFormFileId(1);
        requestDto.setBranchId(UUID.randomUUID());
    }

    @Test
    void saveForm60_happyPath() {
        Branches branch = new Branches();
        branch.setIdentity(requestDto.getBranchId());

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(requestDto.getBranchId())).thenReturn(Optional.of(branch));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));
        when(form60Mapper.toEntity(any(), any(), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto resp = service.saveForm60(requestDto, customerId);
        assertNotNull(resp);
        verify(customerForm60Repository).save(any(CustomerForm60.class));
    }

    @Test
    void saveForm60_customerNotFound_throws() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.saveForm60(requestDto, customerId));
    }

    @Test
    void saveForm60_branchNotFound_throws() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.saveForm60(requestDto, customerId));
    }

    @Test
    void saveForm60_missingCreatedBy_throws() {
        requestDto.setCreatedBy(null);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        assertThrows(BusinessException.class, () -> service.saveForm60(requestDto, customerId));
    }

    @Test
    void saveForm60_vaultReturnsNull_throws() {
        requestDto.setMaskedAdhar("123456789012");
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));
        when(vaultService.generateVaultIdAndMaskAadhaar(any())).thenReturn(null);
        when(form60Mapper.toEntity(any(), any(), any(), any(), any())).thenReturn(form60);

        assertThrows(BusinessException.class, () -> service.saveForm60(requestDto, customerId));
    }

    @Test
    void saveForm60_vaultReturnsStatusN_throws() {
        requestDto.setMaskedAdhar("111122223333");
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));

        FinaVaultResponseDto r = new FinaVaultResponseDto();
        r.setStatus("N");
        r.setErrorCode("ERR001");
        when(vaultService.generateVaultIdAndMaskAadhaar(any())).thenReturn(r);
        when(form60Mapper.toEntity(any(), any(), any(), any(), any())).thenReturn(form60);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveForm60(requestDto, customerId));
        assertNotNull(ex);
    }

    @Test
    void saveForm60_vaultSuccess_setsMaskedAdhar() {
        requestDto.setMaskedAdhar("111122223333");
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));

        FinaVaultResponseDto r = new FinaVaultResponseDto();
        r.setStatus("Y");
        r.setUidForDisplay("XXXX-1234");
        when(vaultService.generateVaultIdAndMaskAadhaar(any())).thenReturn(r);
        when(form60Mapper.toEntity(any(), any(), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(any())).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto out = service.saveForm60(requestDto, customerId);
        assertNotNull(out);
        verify(vaultService, times(1)).generateVaultIdAndMaskAadhaar("111122223333");
    }

    @Test
    void saveForm60_dataIntegrityViolation_convertedToBusinessException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(branchesRepository.findByIdentity(any())).thenReturn(Optional.of(new Branches()));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));
        when(form60Mapper.toEntity(any(), any(), any(), any(), any())).thenReturn(form60);
        when(customerForm60Repository.save(any())).thenThrow(new org.springframework.dao.DataIntegrityViolationException("dup"));

        assertThrows(BusinessException.class, () -> service.saveForm60(requestDto, customerId));
    }

    // ---------------- updateForm60 ----------------

    @Test
    void updateForm60_happyPath() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));
        // updateEntityFromDto will be called on mapper; we don't need to do anything
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(any())).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto resp = service.updateForm60(customerId, form60Id, requestDto);
        assertNotNull(resp);
        verify(customerForm60Repository).save(form60);
    }

    @Test
    void updateForm60_customerNotFound_throws() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.updateForm60(customerId, form60Id, requestDto));
    }

    @Test
    void updateForm60_form60NotFound_throws() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.updateForm60(customerId, form60Id, requestDto));
    }

    @Test
    void updateForm60_vaultReturnsNull_throws() {
        requestDto.setMaskedAdhar("222233334444");
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));
        when(vaultService.generateVaultIdAndMaskAadhaar(any())).thenReturn(null);

        assertThrows(BusinessException.class, () -> service.updateForm60(customerId, form60Id, requestDto));
    }

    @Test
    void updateForm60_vaultReturnsStatusN_throws() {
        requestDto.setMaskedAdhar("222233334444");
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));

        FinaVaultResponseDto r = new FinaVaultResponseDto();
        r.setStatus("N");
        r.setErrorCode("E123");
        when(vaultService.generateVaultIdAndMaskAadhaar(any())).thenReturn(r);

        assertThrows(BusinessException.class, () -> service.updateForm60(customerId, form60Id, requestDto));
    }

    @Test
    void updateForm60_vaultSuccess_and_setsMaskedAdhar() {
        requestDto.setMaskedAdhar("222233334444");
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));

        FinaVaultResponseDto r = new FinaVaultResponseDto();
        r.setStatus("Y");
        r.setUidForDisplay("MASK-1111");
        when(vaultService.generateVaultIdAndMaskAadhaar(any())).thenReturn(r);
        when(customerForm60Repository.save(any())).thenReturn(form60);
        when(form60Mapper.toResponseDto(any())).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto resp = service.updateForm60(customerId, form60Id, requestDto);
        assertNotNull(resp);
        verify(customerForm60Repository).save(form60);
    }

    @Test
    void updateForm60_dataIntegrityViolation_converted() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));
        when(customerForm60Repository.save(any())).thenThrow(new org.springframework.dao.DataIntegrityViolationException("err"));

        assertThrows(BusinessException.class, () -> service.updateForm60(customerId, form60Id, requestDto));
    }

    @Test
    void updateForm60_illegalArgumentConvertedToBusinessException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        // CHANGED: Return Optional.of(documentMaster) instead of Optional.empty()
        when(documentRepository.findByIdentity(any())).thenReturn(Optional.of(documentMaster));
        doThrow(new IllegalArgumentException("bad dto")).when(form60Mapper).updateEntityFromDto(eq(form60), any(), any(), any());

        BusinessException be = assertThrows(BusinessException.class, () -> service.updateForm60(customerId, form60Id, requestDto));
        assertNotNull(be);
    }

    @Test
    void getForm60ByIdentity_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(new CustomerForm60ResponseDto());

        CustomerForm60ResponseDto dto = service.getForm60ByIdentity(customerId, form60Id);
        assertNotNull(dto);
    }

    @Test
    void getForm60ByIdentity_customerNotFound_throws() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.getForm60ByIdentity(customerId, form60Id));
    }

    @Test
    void getForm60ByIdentity_form60NotFound_throws() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.getForm60ByIdentity(customerId, form60Id));
    }

    @Test
    void generateForm60PreviewPdf_success() throws Exception {
        CustomerForm60ResponseDto dto = new CustomerForm60ResponseDto();
        CustomerAddressDetailDto addressDto = new CustomerAddressDetailDto();
        CustomerPurposeResponseDto purposeDto = new CustomerPurposeResponseDto();
        CustomerDesignationResponseDto designationDto = new CustomerDesignationResponseDto();
        Branches branch = new Branches();
        branch.setPlaceName("TestPlace");
        form60.setBranchId(branch);

        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        when(form60Mapper.toResponseDto(form60)).thenReturn(dto);

        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.of(new AddressType()));
        when(customerAddressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(any(), any()))
                .thenReturn(Collections.singletonList(new CustomerAddress()));
        when(customerAddressMapper.mapToCustomerAddressDetailDto(any(), any())).thenReturn(addressDto);

        CustomerProfileExtra extra = new CustomerProfileExtra();
        Purpose purpose = new Purpose();
        purpose.setPurposeId(1);
        purpose.setName("Loan");
        purpose.setCode("P001");
        extra.setPurposeId(purpose);
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(extra));

        CustomerEmployment employment = new CustomerEmployment();
        Designations desig = new Designations();
        desig.setDesignationId(1);
        desig.setName("Manager");
        desig.setCode("D1");
        employment.setDesignationId(desig);
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));

        Map<String, Object> jasperParams = new HashMap<>();
        jasperParams.put("k", "v");
        when(jasperForm60Mapper.form60ToJasperDto(dto, customer, addressDto, purposeDto, designationDto, "TestPlace"))
                .thenReturn(jasperParams);

        when(reportGenerator.generate(eq(ReportName.FORM60), any(), isNull(), eq(OutputFormat.PDF)))
                .thenReturn(new byte[]{1, 2, 3});

        when(jasperForm60Mapper.form60ToJasperDto(any(), any(), any(), any(), any(), any()))
                .thenReturn(jasperParams);

        byte[] out = service.generateForm60PreviewPdf(customerId, form60Id);
        assertNotNull(out);
        assertEquals(3, out.length);
    }

    @Test
    void generateForm60PreviewPdf_customerNotFound_throws() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.generateForm60PreviewPdf(customerId, form60Id));
    }

    private Optional<CustomerEmployment> customerEmployment_repository_stub(CustomerEmploymentRepository repo, Customer c) {
        return Optional.ofNullable(null);
    }

    @Test
    void getCustomerDesignation_success() {
        CustomerEmployment employment = new CustomerEmployment();
        Designations desig = new Designations();
        desig.setDesignationId(10);
        desig.setCode("D10");
        desig.setName("Lead");
        employment.setDesignationId(desig);

        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.of(employment));

        CustomerDesignationResponseDto dto = service.getCustomerDesignation(customer);
        assertNotNull(dto);
        assertEquals("Lead", dto.getName());
    }

    @Test
    void getCustomerDesignation_notFound_throws() {
        when(customerEmploymentRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getCustomerDesignation(customer));
    }

    @Test
    void getPermanentAddress_success() {
        AddressType t = new AddressType();
        CustomerAddress address = new CustomerAddress();
        address.setIdentity(UUID.randomUUID());
        address.setDoorNumber("12A");
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.of(t));
        when(customerAddressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(customer, t))
                .thenReturn(Collections.singletonList(address));
        when(customerAddressMapper.mapToCustomerAddressDetailDto(customer, address)).thenReturn(new CustomerAddressDetailDto());

        CustomerAddressDetailDto dto = service.getPermanentAddress(customer);
        assertNotNull(dto);
    }

    @Test
    void getPermanentAddress_addressTypeMissing_throws() {
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getPermanentAddress(customer));
    }

    @Test
    void getCustomerPurpose_success() {
        CustomerProfileExtra extra = new CustomerProfileExtra();
        Purpose p = new Purpose();
        p.setPurposeId(5);
        p.setName("Gold Loan");
        p.setCode("GOLD");
        extra.setPurposeId(p);
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.of(extra));

        CustomerPurposeResponseDto dto = service.getCustomerPurpose(customer);
        assertNotNull(dto);
        assertEquals("Gold Loan", dto.getName());
    }

    @Test
    void getCustomerPurpose_notFound_throws() {
        when(customerProfileExtraRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getCustomerPurpose(customer));
    }

    @Test
    void getBranchPlaceByForm60Identity_success() {
        Branches b = new Branches();
        b.setPlaceName("Ernakulam");
        form60.setBranchId(b);
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        String place = service.getBranchPlaceByForm60Identity(form60Id);
        assertEquals("Ernakulam", place);
    }

    @Test
    void getBranchPlaceByForm60Identity_notFound_throws() {
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getBranchPlaceByForm60Identity(form60Id));
    }

    @Test
    void uploadSignedForm60_success() {
        Form60UploadDto uploadDto = new Form60UploadDto();
        uploadDto.setDocRefId("PDF-REF-1");
        uploadDto.setFilePath("/tmp/pdf1.pdf");

        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.of(form60));
        when(customerForm60Repository.save(any())).thenReturn(form60);

        Form60UploadResponseDto resp = service.uploadSignedForm60(customerId, form60Id, uploadDto);
        assertNotNull(resp);
        assertEquals(form60Id, resp.getForm60Identity());
        assertEquals(form60.getFilePath(), resp.getFilePath());
    }

    @Test
    void uploadSignedForm60_customerNotFound_throws() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.uploadSignedForm60(customerId, form60Id, new Form60UploadDto()));
    }

    @Test
    void uploadSignedForm60_form60NotFound_throws() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(customerForm60Repository.findByIdentity(form60Id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.uploadSignedForm60(customerId, form60Id, new Form60UploadDto()));
    }
}