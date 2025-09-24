//package com.incede.nbfc.core.monolith.customer.controller;
//
//import com.incede.nbfc.core.monolith.customer.dto.CustomerContactRequestDto;
//import com.incede.nbfc.core.monolith.customer.dto.CustomerContactResponseDto;
//import com.incede.nbfc.core.monolith.customer.service.CustomerContactService;
//import com.incede.nbfc.core.monolith.exception.BusinessException;
//import com.incede.nbfc.core.monolith.exception.ErrorCodes;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Collections;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CustomerContactControllerTest {
//
//    @Mock
//    private CustomerContactService contactService;
//
//    @InjectMocks
//    private CustomerContactController controller;
//
//    private UUID customerId;
//    private UUID contactId;
//    private CustomerContactRequestDto requestDto;
//    private CustomerContactResponseDto responseDto;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        customerId = UUID.randomUUID();
//        contactId = UUID.randomUUID();
//
//        requestDto = CustomerContactRequestDto.builder()
//                .contactType(1)
//                .contactDetails("9876543210")
//                .isPrimary(true)
//                .isActive(true)
//                .isOptOutPromotionalNotification(false)
//                .build();
//
//        responseDto = CustomerContactResponseDto.builder()
//                .identity(contactId)
//                .contacts(Collections.emptyList())
//                .build();
//    }
//
//
//    @Test
//    void testCreateContact() {
//        when(contactService.saveContact(eq(customerId), any(CustomerContactRequestDto.class)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<CustomerContactResponseDto> result =
//                controller.createContact(customerId, requestDto);
//
//        assertEquals(HttpStatus.CREATED, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//    }
//
//    @Test
//    void testUpdateContact() {
//        when(contactService.updateContact(eq(customerId), eq(contactId), any(CustomerContactRequestDto.class)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<CustomerContactResponseDto> result =
//                controller.updateContact(customerId, contactId, requestDto);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//    }
//
//    @Test
//    void testGetContacts() {
//        when(contactService.getContacts(eq(customerId)))
//                .thenReturn(responseDto);
//
//        ResponseEntity<CustomerContactResponseDto> result =
//                controller.getContacts(customerId);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(responseDto, result.getBody());
//    }
//
//
//    @Test
//    void testCreateContact_BusinessException() {
//        when(contactService.saveContact(eq(customerId), any(CustomerContactRequestDto.class)))
//                .thenThrow(new BusinessException("Not found", ErrorCodes.RESOURCE_NOT_FOUND));
//
//        BusinessException ex = assertThrows(BusinessException.class, () ->
//                controller.createContact(customerId, requestDto)
//        );
//
//        assertEquals("Not found", ex.getMessage());
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
//    }
//
//    @Test
//    void testUpdateContact_BusinessException() {
//        when(contactService.updateContact(eq(customerId), eq(contactId), any(CustomerContactRequestDto.class)))
//                .thenThrow(new BusinessException("Conflict", ErrorCodes.CONFLICT));
//
//        BusinessException ex = assertThrows(BusinessException.class, () ->
//                controller.updateContact(customerId, contactId, requestDto)
//        );
//
//        assertEquals("Conflict", ex.getMessage());
//        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
//    }
//
//    @Test
//    void testGetContacts_BusinessException() {
//        when(contactService.getContacts(eq(customerId)))
//                .thenThrow(new BusinessException("Not found", ErrorCodes.RESOURCE_NOT_FOUND));
//
//        BusinessException ex = assertThrows(BusinessException.class, () ->
//                controller.getContacts(customerId)
//        );
//
//        assertEquals("Not found", ex.getMessage());
//        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
//    }
//}
//
