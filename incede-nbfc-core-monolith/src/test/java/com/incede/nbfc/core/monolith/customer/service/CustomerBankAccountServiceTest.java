//package com.incede.nbfc.core.monolith.customer.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.incede.nbfc.core.monolith.common.CommonConstants;
//import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
//import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerBankAccount;
//import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
//import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
//import com.incede.nbfc.core.monolith.customer.mapper.CustomerBankAccountMapper;
//import com.incede.nbfc.core.monolith.customer.repository.CustomerBankAccountRepository;
//import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
//import com.incede.nbfc.core.monolith.exception.BusinessException;
//import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CustomerBankAccountServiceTest {
//
//    @InjectMocks
//    private CustomerBankAccountService service;
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @Mock
//    private CustomerBankAccountRepository customerBankAccountRepository;
//
//    @Mock
//    private CustomerBankAccountMapper customerBankAccountMapper;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    private CustomerBankAccountRequestDto buildValidRequest() {
//        CustomerBankAccountRequestDto dto = new CustomerBankAccountRequestDto();
//        dto.setBankName("HDFC Bank");
//        dto.setAccountNumber("123456789012");
//        dto.setAccountType(1); // Integer type
//        dto.setAccountHolderName("John Doe");
//        dto.setIfscCode("HDFC0001234");
//        dto.setIsActive(true);
//        dto.setUpiId("john@upi");
//        dto.setUpiVerified(true);
//        dto.setIsPrimary(true);
//        dto.setAccountStatus("Active");
//        dto.setPdStatus("PD_OK");
//        return dto;
//    }
//
//    @Test
//    void createBankAccount_success() throws Exception {
//        UUID identity = UUID.randomUUID();
//        String requestJson = "{}";
//        MockMultipartFile file = new MockMultipartFile("file", "proof.pdf", "application/pdf", new byte[]{1, 2});
//
//        CustomerBankAccountRequestDto requestDto = buildValidRequest();
//        Customer customer = new Customer();
//        customer.setIdentity(identity);
//
//        CustomerBankAccount bankAccount = new CustomerBankAccount();
//        CustomerBankAccountResponseDto.BankAccount accountDetail = new CustomerBankAccountResponseDto.BankAccount();
//
//        when(objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class)).thenReturn(requestDto);
//        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
//        when(customerBankAccountRepository.existsByAccountNumberAndCustomer(anyString(), any())).thenReturn(false);
//        when(customerBankAccountRepository.existsByUpiIdAndIsDelFalse(anyString())).thenReturn(false);
//        when(customerBankAccountMapper.toEntity(requestDto)).thenReturn(bankAccount);
//        when(customerBankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
//        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(accountDetail);
//        when(customerBankAccountMapper.toResponse(eq(customer), anyString(), anyList())).thenCallRealMethod();
//
//        CustomerBankAccountResponseDto response = service.createBankAccount(identity, requestJson, file);
//
//        assertNotNull(response);
//        verify(customerBankAccountRepository).save(bankAccount);
//    }
//
//    @Test
//    void createBankAccount_validationFails() throws Exception {
//        UUID identity = UUID.randomUUID();
//        String requestJson = "{}";
//        MockMultipartFile file = new MockMultipartFile("file", "proof.pdf", "application/pdf", new byte[]{1, 2});
//
//        CustomerBankAccountRequestDto requestDto = new CustomerBankAccountRequestDto(); // empty DTO to fail validation
//        when(objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class)).thenReturn(requestDto);
//
//        assertThrows(BusinessException.class, () -> service.createBankAccount(identity, requestJson, file));
//    }
//
//    @Test
//    void createBankAccount_customerNotFound() throws Exception {
//        UUID identity = UUID.randomUUID();
//        String requestJson = "{}";
//        MockMultipartFile file = new MockMultipartFile("file", "proof.pdf", "application/pdf", new byte[]{1, 2});
//
//        CustomerBankAccountRequestDto requestDto = buildValidRequest();
//        when(objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class)).thenReturn(requestDto);
//        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class,
//                () -> service.createBankAccount(identity, requestJson, file));
//    }
//
//    @Test
//    void updateBankAccount_success() {
//        UUID identity = UUID.randomUUID();
//        Integer bankAccountId = 1;
//        CustomerBankAccountRequestDto requestDto = buildValidRequest();
//
//        Customer customer = new Customer();
//        customer.setIdentity(identity);
//
//        CustomerBankAccount bankAccount = new CustomerBankAccount();
//        bankAccount.setCustomer(customer);
//
//        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
//        when(customerBankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccount));
//        when(customerBankAccountRepository.existsByAccountNumberAndCustomerAndBankAccountIdNot(anyString(), any(), anyInt())).thenReturn(false);
//        when(customerBankAccountRepository.existsByUpiIdAndIsDelFalseAndBankAccountIdNot(anyString(), anyInt())).thenReturn(false);
//        when(customerBankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
//        when(customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of(bankAccount));
//        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(new CustomerBankAccountResponseDto.BankAccount());
//        when(customerBankAccountMapper.toResponse(eq(customer), anyString(), anyList())).thenCallRealMethod();
//
//        CustomerBankAccountResponseDto response = service.updateBankAccount(identity, bankAccountId, requestDto);
//
//        assertNotNull(response);
//        verify(customerBankAccountRepository).save(bankAccount);
//    }
//
//    @Test
//    void updateBankAccount_bankAccountNotFound() {
//        UUID identity = UUID.randomUUID();
//        Integer bankAccountId = 1;
//        CustomerBankAccountRequestDto requestDto = buildValidRequest();
//
//        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(new Customer()));
//        when(customerBankAccountRepository.findById(bankAccountId)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> service.updateBankAccount(identity, bankAccountId, requestDto));
//    }
//
//    @Test
//    void getActiveBankAccounts_success() {
//        UUID identity = UUID.randomUUID();
//        Customer customer = new Customer();
//        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
//        CustomerBankAccount bankAccount = new CustomerBankAccount();
//        when(customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of(bankAccount));
//        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(new CustomerBankAccountResponseDto.BankAccount());
//        when(customerBankAccountMapper.toResponse(eq(customer), anyString(), anyList())).thenCallRealMethod();
//
//        CustomerBankAccountResponseDto response = service.getActiveBankAccounts(identity);
//
//        assertNotNull(response);
//    }
//
//    @Test
//    void uploadBankProof_generatesId() {
//        Integer id = service.uploadBankProof(new MockMultipartFile("file", "a.pdf", "application/pdf", new byte[]{1}));
//        assertNotNull(id);
//    }
//}
