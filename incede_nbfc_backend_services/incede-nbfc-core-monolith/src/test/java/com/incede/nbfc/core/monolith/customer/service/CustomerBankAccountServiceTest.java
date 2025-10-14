package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerBankAccount;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountResponseDto.BankAccount;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerBankAccountMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerBankAccountRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountStatuses;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AccountTypeMaster;
import com.incede.nbfc.core.monolith.masterdata.repository.AccountStatusesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AccountTypeMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerBankAccountServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerBankAccountRepository customerBankAccountRepository;
    @Mock
    private CustomerBankAccountMapper customerBankAccountMapper;
    @Mock
    private AccountTypeMasterRepository accountTypeMasterRepository;
    @Mock
    private AccountStatusesRepository accountStatusesRepository;

    @InjectMocks
    private CustomerBankAccountService service;

    private UUID customerId;
    private UUID bankAccountId;
    private Customer customer;
    private CustomerBankAccount bankAccount;
    private CustomerBankAccountRequestDto requestDto;
    private CustomerBankAccountResponseDto responseDto;
    private AccountTypeMaster accountType;
    private AccountStatuses accountStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        bankAccountId = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("CUST001");

        bankAccount = new CustomerBankAccount();
        bankAccount.setIdentity(bankAccountId);
        bankAccount.setCustomer(customer);
        bankAccount.setAccountNumber("1234567890");
        bankAccount.setUpiId("user@upi");

        requestDto = CustomerBankAccountRequestDto.builder()
                .bankName("HDFC Bank")
                .ifscCode("HDFC0001234")
                .accountNumber("1234567890")
                .upiId("user@upi")
                .accountType(UUID.randomUUID())
                .accountStatus(UUID.randomUUID())
                .accountHolderName("John Doe")
                .branchName("Main Branch")
                .bankProofDocumentRefId("123")
                .bankProofFilePath("/path/to/file.jpg")
                .isActive(true)
                .isPrimary(true)
                .upiVerified(true)
                .pdStatus("VERIFIED")
                .pdTxnId("TXN123")
                .customerCode("CUST001")
                .build();

        accountType = new AccountTypeMaster();
        accountType.setIdentity(requestDto.getAccountType());

        accountStatus = new AccountStatuses();
        accountStatus.setIdentity(requestDto.getAccountStatus());

        BankAccount bankAccountDetail = BankAccount.builder()
                .bankName("HDFC Bank")
                .accountHolderName("John Doe")
                .bankAccountIdentity(bankAccountId)
                .build();

        responseDto = CustomerBankAccountResponseDto.builder()
                .identity(customerId)
                .customerCode("CUST001")
                .status("ACTIVE")
                .bankAccounts(List.of(bankAccountDetail))
                .build();
    }

    // --------------------------------------------------------------------------------
    // CREATE BANK ACCOUNT
    // --------------------------------------------------------------------------------

    @Test
    void testCreateBankAccount_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(accountTypeMasterRepository.findByIdentity(requestDto.getAccountType())).thenReturn(Optional.of(accountType));
        when(accountStatusesRepository.findByIdentity(requestDto.getAccountStatus())).thenReturn(Optional.of(accountStatus));
        when(customerBankAccountMapper.toEntity(requestDto)).thenReturn(bankAccount);
        when(customerBankAccountRepository.save(any(CustomerBankAccount.class))).thenReturn(bankAccount);
        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(responseDto.getBankAccounts().get(0));
        when(customerBankAccountMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(responseDto);

        CustomerBankAccountResponseDto result = service.createBankAccount(customerId, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(customerBankAccountRepository, times(1)).save(any(CustomerBankAccount.class));
    }

    @Test
    void testCreateBankAccount_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                service.createBankAccount(customerId, requestDto)
        );
    }

    @Test
    void testCreateBankAccount_InternalError() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerBankAccountMapper.toEntity(requestDto)).thenThrow(new RuntimeException("Unexpected"));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.createBankAccount(customerId, requestDto)
        );

        assertTrue(ex.getMessage().contains(CommonConstants.FAILED_TO_CREATE_BANK_ACCOUNT));
    }

    // --------------------------------------------------------------------------------
    // UPDATE BANK ACCOUNT
    // --------------------------------------------------------------------------------

    @Test
    void testUpdateBankAccount_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerBankAccountRepository.findByIdentity(bankAccountId)).thenReturn(Optional.of(bankAccount));
        when(accountTypeMasterRepository.findByIdentity(any())).thenReturn(Optional.of(accountType));
        when(accountStatusesRepository.findByIdentity(any())).thenReturn(Optional.of(accountStatus));
        when(customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer))
                .thenReturn(List.of(bankAccount));
        when(customerBankAccountRepository.save(any(CustomerBankAccount.class))).thenReturn(bankAccount);
        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(responseDto.getBankAccounts().get(0));
        when(customerBankAccountMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, List.of(responseDto.getBankAccounts().get(0))))
                .thenReturn(responseDto);

        CustomerBankAccountResponseDto result = service.updateBankAccount(customerId, bankAccountId, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateBankAccount_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                service.updateBankAccount(customerId, bankAccountId, requestDto)
        );
    }

    @Test
    void testUpdateBankAccount_BankAccountNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerBankAccountRepository.findByIdentity(bankAccountId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                service.updateBankAccount(customerId, bankAccountId, requestDto)
        );
    }

    // NEW TESTS 👇 for better coverage
    @Test
    void testUpdateBankAccount_MismatchCustomer() {
        Customer otherCustomer = new Customer();
        otherCustomer.setIdentity(UUID.randomUUID());
        bankAccount.setCustomer(otherCustomer);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerBankAccountRepository.findByIdentity(bankAccountId)).thenReturn(Optional.of(bankAccount));

        assertThrows(BusinessException.class, () ->
                service.updateBankAccount(customerId, bankAccountId, requestDto)
        );
    }

    @Test
    void testUpdateBankAccount_InternalError() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerBankAccountRepository.findByIdentity(bankAccountId)).thenThrow(new RuntimeException("Unexpected DB Error"));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                service.updateBankAccount(customerId, bankAccountId, requestDto)
        );
        assertTrue(ex.getMessage().contains(CommonConstants.FAILED_TO_UPDATE_BANK_ACCOUNT));
    }

    // --------------------------------------------------------------------------------
    // GET ACTIVE BANK ACCOUNTS
    // --------------------------------------------------------------------------------

    @Test
    void testGetActiveBankAccounts_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(customerBankAccountRepository.findByCustomerAndIsActiveTrue(customer))
                .thenReturn(List.of(bankAccount));
        when(customerBankAccountMapper.toAccountDetail(bankAccount)).thenReturn(responseDto.getBankAccounts().get(0));
        when(customerBankAccountMapper.toResponse(customer, CommonConstants.CUSTOMER_STATUS_ACTIVE, List.of(responseDto.getBankAccounts().get(0))))
                .thenReturn(responseDto);

        CustomerBankAccountResponseDto result = service.getActiveBankAccounts(customerId);

        assertNotNull(result);
        assertEquals(responseDto, result);
    }

    @Test
    void testGetActiveBankAccounts_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                service.getActiveBankAccounts(customerId)
        );
    }



    @Test
    void testFetchAccountStatus_NotFound_ThrowsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(accountTypeMasterRepository.findByIdentity(any())).thenReturn(Optional.of(accountType));
        when(accountStatusesRepository.findByIdentity(any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () ->
                service.createBankAccount(customerId, requestDto)
        );
    }
}