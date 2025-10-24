package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerBankAccount;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerBankAccountMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerBankAccountRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountTypeMaster;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountStatuses;
import com.incede.nbfc.core.monolith.masterdata.repository.AccountStatusesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AccountTypeMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerBankAccountServiceTest {

    @InjectMocks
    private CustomerBankAccountService service;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerBankAccountRepository customerBankAccountRepository;

    @Mock
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AccountTypeMasterRepository accountTypeMasterRepository;

    @Mock
    private AccountStatusesRepository accountStatusesRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private CustomerBankAccountRequestDto buildValidRequest() {
        return CustomerBankAccountRequestDto.builder()
                .bankName("HDFC Bank")
                .accountNumber("123456789012")
                .accountType(UUID.randomUUID())
                .accountStatus(UUID.randomUUID())
                .accountHolderName("John Doe")
                .ifscCode("HDFC0001234")
                .isActive(true)
                .upiId("john@upi")
                .upiVerified(true)
                .isPrimary(true)
                .pdStatus("PD_OK")
                .pdTxnId("TXN123")
                .customerCode("CUST001")
                .build();
    }

    @Test
    void createBankAccount_success() throws Exception {
        UUID identity = UUID.randomUUID();
        String requestJson = "{}";
        MockMultipartFile file = new MockMultipartFile("file", "proof.pdf", "application/pdf", new byte[]{1, 2});

        CustomerBankAccountRequestDto requestDto = buildValidRequest();
        Customer customer = new Customer();
        customer.setIdentity(identity);

        CustomerBankAccount bankAccount = new CustomerBankAccount();
        CustomerBankAccountResponseDto.BankAccount accountDetail = new CustomerBankAccountResponseDto.BankAccount();


        when(objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class)).thenReturn(requestDto);


        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));


        when(customerBankAccountRepository.existsByAccountNumberAndCustomer(anyString(), any())).thenReturn(false);
        when(customerBankAccountRepository.existsByUpiIdAndIsDelFalse(anyString())).thenReturn(false);

        // Mapping
        when(customerBankAccountMapper.toEntity(requestDto)).thenReturn(bankAccount);
        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(accountDetail);

        // Master data repositories
        when(accountTypeMasterRepository.findByIdentity(requestDto.getAccountType()))
                .thenReturn(Optional.of(new AccountTypeMaster()));
        when(accountStatusesRepository.findByIdentity(requestDto.getAccountStatus()))
                .thenReturn(Optional.of(new AccountStatuses()));

        // Save
        when(customerBankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        // Response mapping
        when(customerBankAccountMapper.toResponse(eq(customer), anyString(), anyList()))
                .thenReturn(CustomerBankAccountResponseDto.builder()
                        .identity(identity)
                        .customerCode("CUST001")
                        .status("OK")
                        .bankAccounts(List.of(accountDetail))
                        .build());

        CustomerBankAccountResponseDto response = service.createBankAccount(identity, requestJson, file);

        assertNotNull(response);
        assertEquals("CUST001", response.getCustomerCode());
        assertEquals("OK", response.getStatus());
        verify(customerBankAccountRepository).save(bankAccount);
    }

    @Test
    void createBankAccount_validationFails() throws Exception {
        UUID identity = UUID.randomUUID();
        String requestJson = "{}";
        MockMultipartFile file = new MockMultipartFile("file", "proof.pdf", "application/pdf", new byte[]{1, 2});

        CustomerBankAccountRequestDto requestDto = new CustomerBankAccountRequestDto(); // empty DTO

        when(objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class)).thenReturn(requestDto);

        assertThrows(BusinessException.class,
                () -> service.createBankAccount(identity, requestJson, file));
    }

    @Test
    void createBankAccount_customerNotFound() throws Exception {
        UUID identity = UUID.randomUUID();
        String requestJson = "{}";
        MockMultipartFile file = new MockMultipartFile("file", "proof.pdf", "application/pdf", new byte[]{1, 2});

        CustomerBankAccountRequestDto requestDto = buildValidRequest();
        when(objectMapper.readValue(requestJson, CustomerBankAccountRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createBankAccount(identity, requestJson, file));
    }

    @Test
    void updateBankAccount_success() {
        UUID identity = UUID.randomUUID();
        UUID bankAccountIdentity = UUID.randomUUID();
        CustomerBankAccountRequestDto requestDto = buildValidRequest();

        Customer customer = new Customer();
        customer.setIdentity(identity);

        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setCustomer(customer);
        bankAccount.setIdentity(bankAccountIdentity);

        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
        when(customerBankAccountRepository.findByIdentity(bankAccountIdentity)).thenReturn(Optional.of(bankAccount));
        when(customerBankAccountRepository.existsByAccountNumberAndCustomerAndIdentityNot(anyString(), any(), any())).thenReturn(false);
        when(customerBankAccountRepository.existsByUpiIdAndIsDelFalseAndIdentityNot(anyString(), any())).thenReturn(false);

        when(accountTypeMasterRepository.findByIdentity(requestDto.getAccountType()))
                .thenReturn(Optional.of(new AccountTypeMaster()));
        when(accountStatusesRepository.findByIdentity(requestDto.getAccountStatus()))
                .thenReturn(Optional.of(new AccountStatuses()));

        when(customerBankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
        when(customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of(bankAccount));
        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(new CustomerBankAccountResponseDto.BankAccount());
        when(customerBankAccountMapper.toResponse(eq(customer), anyString(), anyList()))
                .thenReturn(CustomerBankAccountResponseDto.builder()
                        .identity(identity)
                        .customerCode("CUST001")
                        .status("UPDATED")
                        .bankAccounts(List.of(new CustomerBankAccountResponseDto.BankAccount()))
                        .build());

        CustomerBankAccountResponseDto response = service.updateBankAccount(identity, bankAccountIdentity, requestDto);

        assertNotNull(response);
        assertEquals("UPDATED", response.getStatus());
        verify(customerBankAccountRepository).save(bankAccount);
    }

    @Test
    void updateBankAccount_bankAccountNotFound() {
        UUID identity = UUID.randomUUID();
        UUID bankAccountIdentity = UUID.randomUUID();
        CustomerBankAccountRequestDto requestDto = buildValidRequest();

        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(new Customer()));
        when(customerBankAccountRepository.findByIdentity(bankAccountIdentity)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateBankAccount(identity, bankAccountIdentity, requestDto));
    }

    @Test
    void getActiveBankAccounts_success() {
        UUID identity = UUID.randomUUID();
        Customer customer = new Customer();
        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
        CustomerBankAccount bankAccount = new CustomerBankAccount();
        when(customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of(bankAccount));
        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(new CustomerBankAccountResponseDto.BankAccount());
        when(customerBankAccountMapper.toResponse(eq(customer), anyString(), anyList()))
                .thenReturn(CustomerBankAccountResponseDto.builder()
                        .identity(identity)
                        .customerCode("CUST001")
                        .status("OK")
                        .bankAccounts(List.of(new CustomerBankAccountResponseDto.BankAccount()))
                        .build());

        CustomerBankAccountResponseDto response = service.getActiveBankAccounts(identity);

        assertNotNull(response);
        assertEquals("OK", response.getStatus());
    }

    @Test
    void uploadBankProof_generatesId() {
        Integer id = service.uploadBankProof(new MockMultipartFile("file", "a.pdf", "application/pdf", new byte[]{1}));
        assertNotNull(id);
    }
}
