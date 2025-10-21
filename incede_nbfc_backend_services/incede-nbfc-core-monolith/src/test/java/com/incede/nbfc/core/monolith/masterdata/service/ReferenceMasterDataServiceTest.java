package com.incede.nbfc.core.monolith.masterdata.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Cities;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.States;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.CitiesMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.DistrictMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.PincodeMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.PostOfficeMapper;
import com.incede.nbfc.core.monolith.masterdata.mapper.StatesMapper;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressProofTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.CitiesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.ContactTypesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.DistrictRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PincodesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PostOfficesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.ResidentialStatusesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.StatesRepository;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import jakarta.persistence.EntityManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ReferenceMasterDataService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class ReferenceMasterDataServiceTest {
    @MockBean
    private AddressProofTypeRepository addressProofTypeRepository;

    @MockBean
    private AddressTypeRepository addressTypeRepository;

    @MockBean
    private CitiesMapper citiesMapper;

    @MockBean
    private CitiesRepository citiesRepository;

    @MockBean
    private ContactTypesRepository contactTypesRepository;

    @MockBean
    private DistrictMapper districtMapper;

    @MockBean
    private DistrictRepository districtRepository;

    @MockBean
    private EntityManager entityManager;

    @MockBean
    private PincodeMapper pincodeMapper;

    @MockBean
    private PincodesRepository pincodesRepository;

    @MockBean
    private PostOfficeMapper postOfficeMapper;

    @MockBean
    private PostOfficesRepository postOfficesRepository;

    @Autowired
    private ReferenceMasterDataService referenceMasterDataService;

    @MockBean
    private ResidentialStatusesRepository residentialStatusesRepository;

    @MockBean
    private StatesMapper statesMapper;

    @MockBean
    private StatesRepository statesRepository;

    @MockBean
    private TenantRepository tenantRepository;

    /**
     * Test {@link ReferenceMasterDataService#getTenantId(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getTenantId(UUID)}
     */
    @Test
    @DisplayName("Test getTenantId(UUID)")
    void testGetTenantId() {
        // Arrange
        when(tenantRepository.findByIdentity(Mockito.<UUID>any()))
                .thenThrow(new BusinessException("An error occurred"));

        // Act and Assert
        assertThrows(
                BusinessException.class, () -> referenceMasterDataService.getTenantId(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getTenantId(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository} {@link TenantRepository#findByIdentity(UUID)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getTenantId(UUID)}
     */
    @Test
    @DisplayName("Test getTenantId(UUID); given TenantRepository findByIdentity(UUID) return empty")
    void testGetTenantId_givenTenantRepositoryFindByIdentityReturnEmpty() {
        // Arrange
        Optional<Tenant> emptyResult = Optional.empty();
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(
                BusinessException.class, () -> referenceMasterDataService.getTenantId(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getTenantId(UUID)}.
     *
     * <ul>
     *   <li>Then return intValue is one.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getTenantId(UUID)}
     */
    @Test
    @DisplayName("Test getTenantId(UUID); then return intValue is one")
    void testGetTenantId_thenReturnIntValueIsOne() {
        // Arrange
        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        Integer actualTenantId = referenceMasterDataService.getTenantId(UUID.randomUUID());

        // Assert
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertEquals(1, actualTenantId.intValue());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressTypes(UUID)")
    void testGetAllAddressTypes() {
        // Arrange
        when(tenantRepository.findByIdentity(Mockito.<UUID>any()))
                .thenThrow(new BusinessException("An error occurred"));

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllAddressTypes(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressTypes(UUID)")
    void testGetAllAddressTypes2() {
        // Arrange
        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenThrow(new BusinessException("An error occurred"));

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllAddressTypes(UUID.randomUUID()));
        verify(addressTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link ArrayList#ArrayList()} add {@link AddressTypeView}.
     *   <li>Then return size is one.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllAddressTypes(UUID); given ArrayList() add AddressTypeView; then return size is one")
    void testGetAllAddressTypes_givenArrayListAddAddressTypeView_thenReturnSizeIsOne() {
        // Arrange
        ArrayList<AddressTypeView> addressTypeViewList = new ArrayList<>();
        addressTypeViewList.add(mock(AddressTypeView.class));
        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(addressTypeViewList);

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<AddressTypeView> actualAllAddressTypes =
                referenceMasterDataService.getAllAddressTypes(UUID.randomUUID());

        // Assert
        verify(addressTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertEquals(1, actualAllAddressTypes.size());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository} {@link TenantRepository#findByIdentity(UUID)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllAddressTypes(UUID); given TenantRepository findByIdentity(UUID) return empty")
    void testGetAllAddressTypes_givenTenantRepositoryFindByIdentityReturnEmpty() {
        // Arrange
        Optional<Tenant> emptyResult = Optional.empty();
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllAddressTypes(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository}.
     *   <li>When {@code null}.
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllAddressTypes(UUID); given TenantRepository; when 'null'; then return Empty")
    void testGetAllAddressTypes_givenTenantRepository_whenNull_thenReturnEmpty() {
        // Arrange
        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        // Act
        List<AddressTypeView> actualAllAddressTypes =
                referenceMasterDataService.getAllAddressTypes(null);

        // Assert
        verify(addressTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(isNull());
        assertTrue(actualAllAddressTypes.isEmpty());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}.
     *
     * <ul>
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressTypes(UUID); then return Empty")
    void testGetAllAddressTypes_thenReturnEmpty() {
        // Arrange
        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<AddressTypeView> actualAllAddressTypes =
                referenceMasterDataService.getAllAddressTypes(UUID.randomUUID());

        // Assert
        verify(addressTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertTrue(actualAllAddressTypes.isEmpty());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressProofTypes(UUID)")
    void testGetAllAddressProofTypes() {
        // Arrange
        when(tenantRepository.findByIdentity(Mockito.<UUID>any()))
                .thenThrow(new BusinessException("An error occurred"));

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllAddressProofTypes(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressProofTypes(UUID)")
    void testGetAllAddressProofTypes2() {
        // Arrange
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(
                Mockito.<Integer>any()))
                .thenThrow(new BusinessException("An error occurred"));

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllAddressProofTypes(UUID.randomUUID()));
        verify(addressProofTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository} {@link TenantRepository#findByIdentity(UUID)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllAddressProofTypes(UUID); given TenantRepository findByIdentity(UUID) return empty")
    void testGetAllAddressProofTypes_givenTenantRepositoryFindByIdentityReturnEmpty() {
        // Arrange
        Optional<Tenant> emptyResult = Optional.empty();
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllAddressProofTypes(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository}.
     *   <li>When {@code null}.
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllAddressProofTypes(UUID); given TenantRepository; when 'null'; then return Empty")
    void testGetAllAddressProofTypes_givenTenantRepository_whenNull_thenReturnEmpty() {
        // Arrange
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(
                Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        // Act
        List<AddressProofTypeView> actualAllAddressProofTypes =
                referenceMasterDataService.getAllAddressProofTypes(null);

        // Assert
        verify(addressProofTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(isNull());
        assertTrue(actualAllAddressProofTypes.isEmpty());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}.
     *
     * <ul>
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressProofTypes(UUID); then return Empty")
    void testGetAllAddressProofTypes_thenReturnEmpty() {
        // Arrange
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(
                Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<AddressProofTypeView> actualAllAddressProofTypes =
                referenceMasterDataService.getAllAddressProofTypes(UUID.randomUUID());

        // Assert
        verify(addressProofTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertTrue(actualAllAddressProofTypes.isEmpty());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}.
     *
     * <ul>
     *   <li>Then return size is one.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllAddressProofTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllAddressProofTypes(UUID); then return size is one")
    void testGetAllAddressProofTypes_thenReturnSizeIsOne() {
        // Arrange
        ArrayList<AddressProofTypeView> addressProofTypeViewList = new ArrayList<>();
        addressProofTypeViewList.add(mock(AddressProofTypeView.class));
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(
                Mockito.<Integer>any()))
                .thenReturn(addressProofTypeViewList);

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<AddressProofTypeView> actualAllAddressProofTypes =
                referenceMasterDataService.getAllAddressProofTypes(UUID.randomUUID());

        // Assert
        verify(addressProofTypeRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertEquals(1, actualAllAddressProofTypes.size());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}
     */
    @Test
    @DisplayName("Test getAllResidentialStatuses(UUID)")
    void testGetAllResidentialStatuses() {
        // Arrange
        when(tenantRepository.findByIdentity(Mockito.<UUID>any()))
                .thenThrow(new BusinessException("An error occurred"));

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllResidentialStatuses(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}
     */
    @Test
    @DisplayName("Test getAllResidentialStatuses(UUID)")
    void testGetAllResidentialStatuses2() {
        // Arrange
        when(residentialStatusesRepository.findByIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenThrow(new BusinessException("An error occurred"));

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllResidentialStatuses(UUID.randomUUID()));
        verify(residentialStatusesRepository).findByIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository} {@link TenantRepository#findByIdentity(UUID)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllResidentialStatuses(UUID); given TenantRepository findByIdentity(UUID) return empty")
    void testGetAllResidentialStatuses_givenTenantRepositoryFindByIdentityReturnEmpty() {
        // Arrange
        Optional<Tenant> emptyResult = Optional.empty();
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllResidentialStatuses(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository}.
     *   <li>When {@code null}.
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllResidentialStatuses(UUID); given TenantRepository; when 'null'; then return Empty")
    void testGetAllResidentialStatuses_givenTenantRepository_whenNull_thenReturnEmpty() {
        // Arrange
        when(residentialStatusesRepository.findByIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        // Act
        List<ResidentialStatusesView> actualAllResidentialStatuses =
                referenceMasterDataService.getAllResidentialStatuses(null);

        // Assert
        verify(residentialStatusesRepository).findByIsActiveTrueByTenantId(isNull());
        assertTrue(actualAllResidentialStatuses.isEmpty());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}.
     *
     * <ul>
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}
     */
    @Test
    @DisplayName("Test getAllResidentialStatuses(UUID); then return Empty")
    void testGetAllResidentialStatuses_thenReturnEmpty() {
        // Arrange
        when(residentialStatusesRepository.findByIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<ResidentialStatusesView> actualAllResidentialStatuses =
                referenceMasterDataService.getAllResidentialStatuses(UUID.randomUUID());

        // Assert
        verify(residentialStatusesRepository).findByIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertTrue(actualAllResidentialStatuses.isEmpty());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}.
     *
     * <ul>
     *   <li>Then return size is one.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllResidentialStatuses(UUID)}
     */
    @Test
    @DisplayName("Test getAllResidentialStatuses(UUID); then return size is one")
    void testGetAllResidentialStatuses_thenReturnSizeIsOne() {
        // Arrange
        ArrayList<ResidentialStatusesView> residentialStatusesViewList = new ArrayList<>();
        residentialStatusesViewList.add(mock(ResidentialStatusesView.class));
        when(residentialStatusesRepository.findByIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(residentialStatusesViewList);

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<ResidentialStatusesView> actualAllResidentialStatuses =
                referenceMasterDataService.getAllResidentialStatuses(UUID.randomUUID());

        // Assert
        verify(residentialStatusesRepository).findByIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertEquals(1, actualAllResidentialStatuses.size());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllContactTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllContactTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllContactTypes(UUID)")
    void testGetAllContactTypes() {
        // Arrange
        when(tenantRepository.findByIdentity(Mockito.<UUID>any()))
                .thenThrow(new BusinessException("An error occurred"));

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllContactTypes(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllContactTypes(UUID)}.
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllContactTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllContactTypes(UUID)")
    void testGetAllContactTypes2() {
        // Arrange
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenThrow(new BusinessException("An error occurred"));

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllContactTypes(UUID.randomUUID()));
        verify(contactTypesRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllContactTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link ArrayList#ArrayList()} add {@link ContactTypesView}.
     *   <li>Then return size is one.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllContactTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllContactTypes(UUID); given ArrayList() add ContactTypesView; then return size is one")
    void testGetAllContactTypes_givenArrayListAddContactTypesView_thenReturnSizeIsOne() {
        // Arrange
        ArrayList<ContactTypesView> contactTypesViewList = new ArrayList<>();
        contactTypesViewList.add(mock(ContactTypesView.class));
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(contactTypesViewList);

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<ContactTypesView> actualAllContactTypes =
                referenceMasterDataService.getAllContactTypes(UUID.randomUUID());

        // Assert
        verify(contactTypesRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertEquals(1, actualAllContactTypes.size());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllContactTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository} {@link TenantRepository#findByIdentity(UUID)} return
     *       empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllContactTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllContactTypes(UUID); given TenantRepository findByIdentity(UUID) return empty")
    void testGetAllContactTypes_givenTenantRepositoryFindByIdentityReturnEmpty() {
        // Arrange
        Optional<Tenant> emptyResult = Optional.empty();
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(
                BusinessException.class,
                () -> referenceMasterDataService.getAllContactTypes(UUID.randomUUID()));
        verify(tenantRepository).findByIdentity(isA(UUID.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllContactTypes(UUID)}.
     *
     * <ul>
     *   <li>Given {@link TenantRepository}.
     *   <li>When {@code null}.
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllContactTypes(UUID)}
     */
    @Test
    @DisplayName(
            "Test getAllContactTypes(UUID); given TenantRepository; when 'null'; then return Empty")
    void testGetAllContactTypes_givenTenantRepository_whenNull_thenReturnEmpty() {
        // Arrange
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        // Act
        List<ContactTypesView> actualAllContactTypes =
                referenceMasterDataService.getAllContactTypes(null);

        // Assert
        verify(contactTypesRepository).findByIsDelFalseAndIsActiveTrueByTenantId(isNull());
        assertTrue(actualAllContactTypes.isEmpty());
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllContactTypes(UUID)}.
     *
     * <ul>
     *   <li>Then return Empty.
     * </ul>
     *
     * <p>Method under test: {@link ReferenceMasterDataService#getAllContactTypes(UUID)}
     */
    @Test
    @DisplayName("Test getAllContactTypes(UUID); then return Empty")
    void testGetAllContactTypes_thenReturnEmpty() {
        // Arrange
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(Mockito.<Integer>any()))
                .thenReturn(new ArrayList<>());

        Tenant tenant = new Tenant();
        tenant.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setCreatedBy(1);
        tenant.setIdentity(UUID.randomUUID());
        tenant.setIsActive(true);
        tenant.setIsDel(true);
        tenant.setTenantCode("Tenant Code");
        tenant.setTenantId(1);
        tenant.setTenantName("Tenant Name");
        tenant.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        tenant.setUpdatedBy(1);
        Optional<Tenant> ofResult = Optional.of(tenant);
        when(tenantRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        List<ContactTypesView> actualAllContactTypes =
                referenceMasterDataService.getAllContactTypes(UUID.randomUUID());

        // Assert
        verify(contactTypesRepository).findByIsDelFalseAndIsActiveTrueByTenantId(1);
        verify(tenantRepository).findByIdentity(isA(UUID.class));
        assertTrue(actualAllContactTypes.isEmpty());
    }

//

    /**
     * Test {@link ReferenceMasterDataService#importFile(MultipartFile, Integer)} with valid CSV.
     */
    @Test
    @DisplayName("Test importFile with valid CSV file")
    void testImportFileWithValidCsv() throws IOException {
        // Arrange
        String csvContent = "state,district,pincode,office_name,office_type,delivery,region_name,division_name,latitude,longitude\n" +
                "State1,District1,123456,Office1,Type1,Delivery1,Region1,Division1,12.34,56.78";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(statesRepository.findAll()).thenReturn(new ArrayList<>());
        when(districtRepository.findAll()).thenReturn(new ArrayList<>());
        when(citiesRepository.findByCity("Default")).thenReturn(Optional.empty());
        when(citiesRepository.save(any(Cities.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pincodesRepository.findAll()).thenReturn(new ArrayList<>());
        when(postOfficesRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        referenceMasterDataService.importFile(file, 1);

        // Assert
        verify(entityManager, atLeast(1)).persist(any());
        verify(entityManager, atLeast(1)).flush();
    }

    /**
     * Test {@link ReferenceMasterDataService#importFile(MultipartFile, Integer)} with valid Excel.
     */
    @Test
    @DisplayName("Test importFile with valid Excel file")
    void testImportFileWithValidExcel() throws IOException {
        // Arrange
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"state", "district", "pincode", "office_name", "office_type",
                    "delivery", "region_name", "division_name", "latitude", "longitude"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // Create data row
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("State1");
            dataRow.createCell(1).setCellValue("District1");
            dataRow.createCell(2).setCellValue("123456");
            dataRow.createCell(3).setCellValue("Office1");
            dataRow.createCell(4).setCellValue("Type1");
            dataRow.createCell(5).setCellValue("Delivery1");
            dataRow.createCell(6).setCellValue("Region1");
            dataRow.createCell(7).setCellValue("Division1");
            dataRow.createCell(8).setCellValue("12.34");
            dataRow.createCell(9).setCellValue("56.78");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "test.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    outputStream.toByteArray()
            );

            when(statesRepository.findAll()).thenReturn(new ArrayList<>());
            when(districtRepository.findAll()).thenReturn(new ArrayList<>());
            when(citiesRepository.findByCity("Default")).thenReturn(Optional.empty());
            when(citiesRepository.save(any(Cities.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(pincodesRepository.findAll()).thenReturn(new ArrayList<>());
            when(postOfficesRepository.findAll()).thenReturn(new ArrayList<>());

            // Act
            referenceMasterDataService.importFile(file, 1);

            // Assert
            verify(entityManager, atLeast(1)).persist(any());
        }
    }

    /**
     * Test {@link ReferenceMasterDataService#importFile(MultipartFile, Integer)} with unsupported file type.
     */
    @Test
    @DisplayName("Test importFile with unsupported file type")
    void testImportFileWithUnsupportedFileType() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "content".getBytes()
        );

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                referenceMasterDataService.importFile(file, 1));
    }

    /**
     * Test {@link ReferenceMasterDataService#parseCsv(MultipartFile)} with valid CSV.
     */
    @Test
    @DisplayName("Test parseCsv with valid CSV content")
    void testParseCsvWithValidContent() throws IOException {
        // Arrange
        String csvContent = "state,district,pincode,office_name,office_type,delivery,region_name,division_name,latitude,longitude\n" +
                "State1,District1,123456,Office1,Type1,Delivery1,Region1,Division1,12.34,56.78";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // Act
        List<PincodeRowDTO> result = referenceMasterDataService.parseCsv(file);

        // Assert
        assertEquals(1, result.size());
        PincodeRowDTO dto = result.get(0);
        assertEquals("State1", dto.getState());
        assertEquals("District1", dto.getDistrict());
        assertEquals("123456", dto.getPincode());
        assertEquals("Office1", dto.getOfficeName());
    }

    /**
     * Test {@link ReferenceMasterDataService#parseCsv(MultipartFile)} with missing headers.
     */
    @Test
    @DisplayName("Test parseCsv with missing required headers")
    void testParseCsvWithMissingHeaders() {
        // Arrange
        String csvContent = "state,district,pincode\n" + // Missing office_name
                "State1,District1,123456";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                referenceMasterDataService.parseCsv(file));
    }


    /**
     * Test {@link ReferenceMasterDataService (String)} (String)} (String)} (String)} with valid and invalid values.
     */
    @Test
    @DisplayName("Test parseBigDecimal with various inputs")
    void testParseBigDecimal() {
        // This tests the BigDecimal parsing logic through the import flow
        String csvContent = "state,district,pincode,office_name,latitude,longitude\n" +
                "State1,District1,123456,Office1,12.34,56.78\n" + // valid
                "State2,District2,654321,Office2,invalid,invalid"; // invalid

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(statesRepository.findAll()).thenReturn(new ArrayList<>());
        when(districtRepository.findAll()).thenReturn(new ArrayList<>());
        when(citiesRepository.findByCity("Default")).thenReturn(Optional.empty());
        when(citiesRepository.save(any(Cities.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(pincodesRepository.findAll()).thenReturn(new ArrayList<>());
        when(postOfficesRepository.findAll()).thenReturn(new ArrayList<>());

        // Act - should not throw exception for invalid BigDecimal values
        assertDoesNotThrow(() -> referenceMasterDataService.importFile(file, 1));
    }

    /**
     * Test {@link ReferenceMasterDataService#getAllPincodes(int, int)} with empty result.
     */
    @Test
    @DisplayName("Test getAllPincodes with empty result")
    void testGetAllPincodesEmpty() {
        // Arrange
        when(pincodesRepository.findByIsDelFalse(any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        Page<PincodeDto> result = referenceMasterDataService.getAllPincodes(0, 10);

        // Assert
        assertTrue(result.isEmpty());
        verify(pincodesRepository).findByIsDelFalse(any(Pageable.class));
    }

    /**
     * Test {@link ReferenceMasterDataService#getPincodeDetails(String)} with multiple pincodes.
     */
    @Test
    @DisplayName("Test getPincodeDetails with multiple matching pincodes")
    void testGetPincodeDetailsMultiple() {
        // Arrange
        Pincodes pincode1 = createTestPincode(1, "123456");
        Pincodes pincode2 = createTestPincode(2, "123456");

        List<Pincodes> pincodesList = Arrays.asList(pincode1, pincode2);

        when(pincodesRepository.findByPincodeWithDetails("123456"))
                .thenReturn(pincodesList);
        when(postOfficesRepository.findByPincode_PincodeIdIn(anyList()))
                .thenReturn(new ArrayList<>());
        when(pincodeMapper.convertToDto(any(Pincodes.class)))
                .thenReturn(new PincodeDto());

        // Act
        List<PincodeDto> result = referenceMasterDataService.getPincodeDetails("123456");

        // Assert
        assertEquals(2, result.size());
        verify(pincodesRepository).findByPincodeWithDetails("123456");
        verify(postOfficesRepository).findByPincode_PincodeIdIn(anyList());
    }

    /**
     * Test all get methods with null tenant identity.
     */
    @Test
    @DisplayName("Test all get methods with null tenant identity")
    void testAllGetMethodsWithNullTenant() {
        // Setup common mocks for null tenant scenario
        when(addressTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(new ArrayList<>());
        when(addressProofTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(new ArrayList<>());
        when(residentialStatusesRepository.findByIsActiveTrueByTenantId(null))
                .thenReturn(new ArrayList<>());
        when(contactTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(null))
                .thenReturn(new ArrayList<>());

        // Act & Assert - all should return empty lists without throwing exceptions
        assertDoesNotThrow(() -> {
            referenceMasterDataService.getAllAddressTypes(null);
            referenceMasterDataService.getAllAddressProofTypes(null);
            referenceMasterDataService.getAllResidentialStatuses(null);
            referenceMasterDataService.getAllContactTypes(null);
        });
    }

    /**
     * Test {@link ReferenceMasterDataService#importFile(MultipartFile, Integer)} with file size exceeding limit.
     */
    @Test
    @DisplayName("Test importFile with file size exceeding limit")
    void testImportFileWithSizeExceedingLimit() {
        // Arrange
        MockMultipartFile file = mock(MockMultipartFile.class);
        when(file.getSize()).thenReturn(60 * 1024 * 1024L); // 60MB > 50MB limit
        when(file.getOriginalFilename()).thenReturn("test.csv");

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                referenceMasterDataService.importFile(file, 1));
    }

    // Helper method to create test Pincodes
    private Pincodes createTestPincode(Integer id, String pincode) {
        Pincodes pincodeEntity = new Pincodes();
        pincodeEntity.setPincodeId(id);
        pincodeEntity.setPincode(pincode);
        pincodeEntity.setIdentity(UUID.randomUUID());

        States state = new States();
        state.setState("Test State");
        pincodeEntity.setStates(state);

        Districts district = new Districts();
        district.setDistrict("Test District");
        pincodeEntity.setDistricts(district);

        return pincodeEntity;
    }

    /**
     * Test {@link ReferenceMasterDataService#parseCsv(MultipartFile)} with empty file.
     */
    @Test
    @DisplayName("Test parseCsv with empty file")
    void testParseCsvWithEmptyFile() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                new byte[0]
        );

        // Act & Assert
        assertThrows(BusinessException.class, () ->
                referenceMasterDataService.parseCsv(file));
    }

    /**
     * Test {@link ReferenceMasterDataService#getPincodeDetails(String)} with post offices.
     */
    @Test
    @DisplayName("Test getPincodeDetails with post offices")
    void testGetPincodeDetailsWithPostOffices() {
        // Arrange
        Pincodes pincode = createTestPincode(1, "123456");

        PostOffices postOffice = new PostOffices();
        postOffice.setOfficeName("Test Office");
        postOffice.setIdentity(UUID.randomUUID());
        postOffice.setPincode(pincode);

        List<Pincodes> pincodesList = List.of(pincode);
        List<PostOffices> postOfficesList = List.of(postOffice);

        when(pincodesRepository.findByPincodeWithDetails("123456"))
                .thenReturn(pincodesList);
        when(postOfficesRepository.findByPincode_PincodeIdIn(anyList()))
                .thenReturn(postOfficesList);

        PincodeDto pincodeDto = new PincodeDto();
        pincodeDto.setPincode("123456");
        when(pincodeMapper.convertToDto(any(Pincodes.class)))
                .thenReturn(pincodeDto);

        // Act
        List<PincodeDto> result = referenceMasterDataService.getPincodeDetails("123456");

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPostOffices().size());
        assertEquals("Test Office", result.get(0).getPostOffices().get(0).getOfficeName());
    }
}
