package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.masterdata.dto.AddressProofTypeView;
import com.incede.nbfc.core.monolith.masterdata.dto.AddressTypeView;
import com.incede.nbfc.core.monolith.masterdata.dto.ContactTypesView;
import com.incede.nbfc.core.monolith.masterdata.dto.ResidentialStatusesView;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressProofTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.ContactTypesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.ResidentialStatusesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReferenceMasterDataServiceTest {

    @Mock
    AddressTypeRepository addressTypeRepository;
    @Mock
    AddressProofTypeRepository addressProofTypeRepository;
    @Mock
    ResidentialStatusesRepository residentialStatusesRepository;
    @Mock
    ContactTypesRepository contactTypesRepository;

    @InjectMocks
    ReferenceMasterDataService referenceMasterDataService;


    @Test
    void testGetAllAddressTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        AddressTypeView mockView = mock(AddressTypeView.class);
        when(mockView.getAddressTypeName()).thenReturn("Office");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<AddressTypeView> result = referenceMasterDataService.getAllAddressTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Office", result.get(0).getAddressTypeName());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAddressTypes_WhenDataEmpty() {
        lenient().when(addressTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<AddressTypeView> result = referenceMasterDataService.getAllAddressTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAddressTypes_WhenRepositoryThrowsException() {
        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllAddressTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllAddressProofTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        AddressProofTypeView mockView = mock(AddressProofTypeView.class);
        when(mockView.getCode()).thenReturn("PASSPORT");
        when(mockView.getName()).thenReturn("Passport");
        when(mockView.getConciseDescription()).thenReturn("Passport Document");
        when(mockView.getIdentity()).thenReturn(id);

        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<AddressProofTypeView> result = referenceMasterDataService.getAllAddressProofTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PASSPORT", result.get(0).getCode());
        assertEquals("Passport", result.get(0).getName());
        assertEquals("Passport Document", result.get(0).getConciseDescription());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllAddressProofTypes_WhenDataEmpty() {
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<AddressProofTypeView> result = referenceMasterDataService.getAllAddressProofTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAddressProofTypes_WhenRepositoryThrowsException() {
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllAddressProofTypes());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllResidentialStatuses_WhenDataExists() {
        UUID id = UUID.randomUUID();

        ResidentialStatusesView mockView = mock(ResidentialStatusesView.class);
        when(mockView.getName()).thenReturn("Owned");
        when(mockView.getCode()).thenReturn("OWN");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(residentialStatusesRepository.findByIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<ResidentialStatusesView> result = referenceMasterDataService.getAllResidentialStatuses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Owned", result.get(0).getName());
        assertEquals("OWN", result.get(0).getCode());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllResidentialStatuses_WhenDataEmpty() {
        when(residentialStatusesRepository.findByIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<ResidentialStatusesView> result = referenceMasterDataService.getAllResidentialStatuses();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllResidentialStatuses_WhenRepositoryThrowsException() {
        when(residentialStatusesRepository.findByIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllResidentialStatuses());

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void testGetAllContactTypes_WhenDataExists() {
        UUID id = UUID.randomUUID();

        ContactTypesView mockView = mock(ContactTypesView.class);
        when(mockView.getContactType()).thenReturn("Mobile");
        when(mockView.getIsActive()).thenReturn(true);
        when(mockView.getIdentity()).thenReturn(id);

        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(List.of(mockView));

        List<ContactTypesView> result = referenceMasterDataService.getAllContactTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mobile", result.get(0).getContactType());
        assertTrue(result.get(0).getIsActive());
        assertEquals(id, result.get(0).getIdentity());
    }

    @Test
    void testGetAllContactTypes_WhenDataEmpty() {
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenReturn(Collections.emptyList());

        List<ContactTypesView> result = referenceMasterDataService.getAllContactTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllContactTypes_WhenRepositoryThrowsException() {
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrue())
                .thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> referenceMasterDataService.getAllContactTypes());

        assertEquals("DB error", ex.getMessage());
    }

}
