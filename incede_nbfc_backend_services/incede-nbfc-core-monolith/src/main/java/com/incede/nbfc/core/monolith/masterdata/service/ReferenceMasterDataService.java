package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.mapper.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReferenceMasterDataService {

    private final AddressTypeRepository addressTypeRepository;
    private final AddressProofTypeRepository addressProofTypeRepository;
    private final ResidentialStatusesRepository residentialStatusesRepository;
    private final ContactTypesRepository contactTypesRepository;
    private final PincodesRepository pincodesRepository;
    private final PostOfficesRepository postOfficesRepository;

    private final StatesRepository statesRepository;
    private final DistrictRepository districtRepository;
    private final CitiesRepository citiesRepository;
    private final TenantRepository tenantRepository;

    private final PincodeMapper pincodeMapper;
    private final StatesMapper statesMapper;
    private final DistrictMapper districtMapper;
    private final CitiesMapper citiesMapper;
    private final PostOfficeMapper postOfficeMapper;

    @Autowired
    private EntityManager entityManager;




    /**
     * find tenant from Tenant Identity
     *
     */
    public Integer getTenantId(UUID tenantIdentity){
        Tenant tenant = tenantRepository.findByIdentity(tenantIdentity).orElseThrow(
                ()-> new BusinessException(CommonConstants.TENANT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));
        return tenant.getTenantId();

    }

    /**
     * Retrieves all active address p
     *
     */
    @Transactional(readOnly = true)
    public List<AddressTypeView> getAllAddressTypes(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active address by tenant Id={}",tenantId);

        List<AddressTypeView> addressType = addressTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);

        if (addressType.isEmpty()) {
            log.warn("No address types found");
            return addressType;
        }
        log.info("Fetched {} address types", addressType.size());
        return Collections.unmodifiableList(addressType);
    }

    /**
     * Retrieves all active address proof Types
     *
     */
    @Transactional(readOnly = true)
    public List<AddressProofTypeView> getAllAddressProofTypes(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active address proof Types by tenant Id={}",tenantId);

        List<AddressProofTypeView> addressProofType = addressProofTypeRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);

        if (addressProofType.isEmpty()) {
            log.warn("No Address proof types  found");
            return addressProofType;
        }

        log.info("Fetched {} Address proof types", addressProofType.size());
        return Collections.unmodifiableList(addressProofType);

    }

    /**
     * Retrieves all active Residential statuses
     *
     */
    @Transactional(readOnly = true)
    public List<ResidentialStatusesView> getAllResidentialStatuses(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active Residential statuses by tenant Id={}",tenantId);
        List<ResidentialStatusesView> residentialStatuses = residentialStatusesRepository.findByIsActiveTrueByTenantId(tenantId);
        if (residentialStatuses.isEmpty()) {
            log.warn("No residential statuses found");
            return residentialStatuses;
        }
        log.info("Fetched {} residential statuses", residentialStatuses.size());
        return Collections.unmodifiableList(residentialStatuses);
    }

    /**
     * Retrieves all active contact Types
     *
     */
    @Transactional(readOnly = true)
    public List<ContactTypesView> getAllContactTypes(UUID tenantIdentity) {

        Integer tenantId = null;
        if(tenantIdentity!=null){
            tenantId = getTenantId(tenantIdentity);
        }
        log.info("Fetching all active contact Types by tenant Id={}",tenantId);
        List<ContactTypesView> contactTypes = contactTypesRepository.findByIsDelFalseAndIsActiveTrueByTenantId(tenantId);

        if (contactTypes.isEmpty()) {
            log.warn("No contact types found");
            return contactTypes;
        }
        log.info("Fetched {} contact types", contactTypes.size());
        return Collections.unmodifiableList(contactTypes);
    }

    /**
     * Get details for a specific pincode including city, state, district, and post office names
     */
    @Transactional(readOnly = true)
    public List<PincodeDto> getPincodeDetails(String pincodeNumber) {
        List<Pincodes> entities = pincodesRepository.findByPincodeWithDetails(pincodeNumber);

        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> pincodeIds = entities.stream()
                .map(Pincodes::getPincodeId)
                .toList();

        List<PostOffices> postOffices = postOfficesRepository.findByPincode_PincodeIdIn(pincodeIds);

        Map<Integer, List<PostOfficesResponseDto>> postOfficeMap = postOffices.stream()
                .collect(Collectors.groupingBy(
                        po -> po.getPincode().getPincodeId(),
                        Collectors.mapping(po -> new PostOfficesResponseDto(po.getOfficeName(), po.getIdentity()), Collectors.toList())
                ));

        return entities.stream()
                .map(pincode -> {
                    PincodeDto dto = pincodeMapper.convertToDto(pincode);
                    dto.setStateName(pincode.getStates().getState());
                    dto.setDistrictName(pincode.getDistricts().getDistrict());
                    dto.setIdentity(pincode.getIdentity());

                    List<PostOfficesResponseDto> officeDtos = postOfficeMap.getOrDefault(pincode.getPincodeId(), List.of());
                    dto.setPostOffices(officeDtos);

                    return dto;
                })
                .toList();
    }

        /**
         * Retrieves all pincdes
         *
         */
        @Transactional(readOnly = true)
        public Page<PincodeDto> getAllPincodes ( int page, int size){
            Pageable pageable = PageRequest.of(page, size);

            Page<Pincodes> pincodesPage = pincodesRepository.findByIsDelFalse(pageable);

            return pincodesPage.map(pincode -> {
                PincodeDto dto = pincodeMapper.convertToDto(pincode);

//                if (pincode.getCities() != null) {
//                    dto.setCityName(pincode.getCities().getCity());
//                }
                if (pincode.getStates() != null) {
                    dto.setStateName(pincode.getStates().getState());
                }
                if (pincode.getDistricts() != null) {
                    dto.setDistrictName(pincode.getDistricts().getDistrict());
                }
                return dto;
            });
        }

    /**
     * upload all pincdes
     *
     */
    @Transactional
    public void importFile(MultipartFile file, Integer createdBy) {
        final Integer createdByFinal = createdBy;
        long maxFileSize = CommonConstants.MAXIMUM_FILE_SIZE; // 50MB limit (same as application.yml)
        if (file.getSize() > maxFileSize) {
            log.error("File size exceeded limit: {} bytes (max allowed: {} bytes)", file.getSize(), maxFileSize);
            throw new BusinessException("File size exceeds the maximum limit of 50MB", ErrorCodes.INVALID_REQUEST_FORMAT);
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".csv") && !filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            throw new BusinessException("Unsupported file type. Only CSV and Excel files are allowed.", ErrorCodes.INVALID_REQUEST_FORMAT);
        }

        List<PincodeRowDTO> pincodeRows;
        try {
            pincodeRows = file.getOriginalFilename().endsWith(".csv")
                    ? parseCsv(file)
                    : parseExcel(file);
        } catch (BusinessException e) {
            // re-throw the business exception so that your global handler can process it
            throw e;
        } catch (Exception e) {
            log.error("Failed to parse file: {}", file.getOriginalFilename(), e);
            throw new BusinessException(
                    "File parsing failed: unsupported or invalid format",
                    ErrorCodes.INVALID_REQUEST_FORMAT
            );
        }

        Map<String, States> stateCache;
        Map<String, Districts> districtCache;
        Cities defaultCity;
        Map<String, Pincodes> pincodeCache;
        Set<String> postOfficeKeys;

        try {
            stateCache = statesRepository.findAll().stream()
                    .collect(Collectors.toMap(state -> state.getState().toLowerCase(), Function.identity()));

            districtCache = districtRepository.findAll().stream()
                    .collect(Collectors.toMap(district -> district.getDistrict().toLowerCase(), Function.identity()));

            defaultCity = citiesRepository.findByCity("Default")
                    .orElseGet(() -> citiesRepository.save(new Cities(null, null, "Default", true, UUID.randomUUID())));

            pincodeCache = pincodesRepository.findAll().stream()
                    .collect(Collectors.toMap(Pincodes::getPincode, Function.identity()));

            postOfficeKeys = postOfficesRepository.findAll().stream()
                    .map(postOffice -> postOffice.getOfficeName().toLowerCase() + "|" + postOffice.getPincode().getPincode())
                    .collect(Collectors.toSet());

        } catch (Exception e) {
            log.error("Failed to preload master data", e);
            throw new BusinessException("Failed to preload master data", ErrorCodes.NOT_FOUND);
        }

        int batchSize = 3000;
        int rowCount = 0;
        List<PincodeRowDTO> failedRows = new ArrayList<>();

        for (PincodeRowDTO rowDto : pincodeRows) {
            try {
                // Process State
                String stateKey = rowDto.getState().toLowerCase();
                States stateEntity = stateCache.computeIfAbsent(stateKey, key -> {
                    States newState = new States(null, null, rowDto.getState(), true, UUID.randomUUID());
                    newState.setCreatedBy(createdByFinal);
                    entityManager.persist(newState);
                    return newState;
                });

                // Process District
                String districtKey = rowDto.getDistrict().toLowerCase();
                Districts districtEntity = districtCache.computeIfAbsent(districtKey, key -> {
                    Districts newDistrict = new Districts(null, null, rowDto.getDistrict(), true, UUID.randomUUID());
                    newDistrict.setCreatedBy(createdByFinal);
                    entityManager.persist(newDistrict);
                    return newDistrict;
                });

                // Process Pincode
                Pincodes pincodeEntity = pincodeCache.computeIfAbsent(rowDto.getPincode(), key -> {
                    Pincodes newPincode = new Pincodes();
                    newPincode.setPincode(rowDto.getPincode());
                    newPincode.setLatitude(rowDto.getLatitude());
                    newPincode.setLongitude(rowDto.getLongitude());
                    newPincode.setStates(stateEntity);
                    newPincode.setDistricts(districtEntity);
                    newPincode.setCities(defaultCity);
                    newPincode.setCreatedBy(createdByFinal);
                    newPincode.setIdentity(UUID.randomUUID());
                    entityManager.persist(newPincode);
                    return newPincode;
                });

                // Process Post Office
                String postOfficeKey = rowDto.getOfficeName().toLowerCase() + "|" + rowDto.getPincode();
                if (!postOfficeKeys.contains(postOfficeKey)) {
                    PostOffices newPostOffice = new PostOffices();
                    newPostOffice.setOfficeName(rowDto.getOfficeName());
                    newPostOffice.setOfficeType(rowDto.getOfficeType());
                    newPostOffice.setDeliveryStatus(rowDto.getDeliveryStatus());
                    newPostOffice.setRegion(rowDto.getRegion());
                    newPostOffice.setDivision(rowDto.getDivision());
                    newPostOffice.setLatitude(rowDto.getLatitude());
                    newPostOffice.setLongitude(rowDto.getLongitude());
                    newPostOffice.setPincode(pincodeEntity);
                    newPostOffice.setCreatedBy(createdByFinal);
                    newPostOffice.setIdentity(UUID.randomUUID());
                    entityManager.persist(newPostOffice);
                    postOfficeKeys.add(postOfficeKey);
                }

                rowCount++;
                if (rowCount % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                    log.info("Flushed and cleared batch at row {}", rowCount);
                }

            } catch (Exception e) {
                log.error("Failed to process row: {}", rowDto, e);
                failedRows.add(rowDto);
            }
        }

        try {
            entityManager.flush();
            entityManager.clear();
            log.info("Final flush and clear completed");
        } catch (Exception e) {
            log.error("Final flush failed", e);
            throw new BusinessException("Final flush failed during persistence", ErrorCodes.DATABASE_ERROR);
        }

        if (!failedRows.isEmpty()) {
            log.warn("Import completed with {} failed rows", failedRows.size());
        }
    }

    private List<PincodeRowDTO> parseExcel(MultipartFile file) {
        return parseWorkbook(file);
    }

    List<PincodeRowDTO> parseCsv(MultipartFile file) {
        List<PincodeRowDTO> pincodeRows = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            Set<String> requiredHeaders = Set.of("state", "district", "pincode", "office_name");
            Map<String, Integer> headerMap = csvParser.getHeaderMap().entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            e -> e.getKey().trim().toLowerCase(),
                            Map.Entry::getValue
                    ));

            List<String> missingHeaders = requiredHeaders.stream()
                    .filter(header -> !headerMap.containsKey(header))
                    .toList();

            if (!missingHeaders.isEmpty()) {
                throw new BusinessException(
                        "Missing required CSV headers: " + String.join(", ", missingHeaders),
                        ErrorCodes.INVALID_REQUEST_FORMAT
                );
            }

            int rowNumber = 1;
            for (CSVRecord csvRecord : csvParser) {
                try {
                    pincodeRows.add(buildPincodeRowDTOFromCsv(csvRecord));
                } catch (Exception e) {
                    log.warn("Failed to parse CSV row {}: {}", rowNumber, e.getMessage());
                }
                rowNumber++;
            }

        } catch (IOException e) {
            log.error("CSV parsing failed for file: {}", file.getOriginalFilename(), e);
            throw new BusinessException("CSV parsing failed", ErrorCodes.INVALID_REQUEST_FORMAT);
        }
        return pincodeRows;
    }

    private PincodeRowDTO buildPincodeRowDTOFromCsv(CSVRecord csvRecord) {
        PincodeRowDTO rowDto = new PincodeRowDTO();
        rowDto.setState(csvRecord.get("state").trim());
        rowDto.setDistrict(csvRecord.get("district").trim());
        rowDto.setPincode(csvRecord.get("pincode").trim());
        rowDto.setOfficeName(csvRecord.get("office_name").trim());
        rowDto.setOfficeType(csvRecord.get("office_type").trim());
        rowDto.setDeliveryStatus(csvRecord.get("delivery").trim());
        rowDto.setRegion(csvRecord.get("region_name").trim());
        rowDto.setDivision(csvRecord.get("division_name").trim());
        rowDto.setLatitude(parseBigDecimal(csvRecord.get("latitude")));
        rowDto.setLongitude(parseBigDecimal(csvRecord.get("longitude")));
        return rowDto;
    }

    private List<PincodeRowDTO> parseWorkbook(MultipartFile file) {
        List<PincodeRowDTO> pincodeRows = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() == 0) return pincodeRows;

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                headerMap.put(cell.getStringCellValue().trim().toLowerCase(), cell.getColumnIndex());
            }

            List<String> requiredHeaders = List.of("state", "district", "pincode", "office_name");
            List<String> missingHeaders = requiredHeaders.stream()
                    .filter(header -> !headerMap.containsKey(header))
                    .toList();

            if (!missingHeaders.isEmpty()) {
                throw new BusinessException(
                        "Missing required Excel headers: " + String.join(", ", missingHeaders),
                        ErrorCodes.INVALID_REQUEST_FORMAT
                );
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row excelRow = sheet.getRow(rowIndex);
                if (excelRow == null) continue;

                try {
                    PincodeRowDTO rowDto = new PincodeRowDTO();
                    rowDto.setState(getCellValue(excelRow.getCell(headerMap.get("state"))));
                    rowDto.setDistrict(getCellValue(excelRow.getCell(headerMap.get("district"))));
                    rowDto.setPincode(getCellValue(excelRow.getCell(headerMap.get("pincode"))));
                    rowDto.setOfficeName(getCellValue(excelRow.getCell(headerMap.get("office_name"))));
                    rowDto.setOfficeType(getCellValue(excelRow.getCell(headerMap.get("office_type"))));
                    rowDto.setDeliveryStatus(getCellValue(excelRow.getCell(headerMap.get("delivery"))));
                    rowDto.setRegion(getCellValue(excelRow.getCell(headerMap.get("region_name"))));
                    rowDto.setDivision(getCellValue(excelRow.getCell(headerMap.get("division_name"))));
                    rowDto.setLatitude(parseBigDecimal(getCellValue(excelRow.getCell(headerMap.get("latitude")))));
                    rowDto.setLongitude(parseBigDecimal(getCellValue(excelRow.getCell(headerMap.get("longitude")))));

                    pincodeRows.add(rowDto);
                } catch (Exception e) {
                    log.warn("Failed to parse Excel row {}: {}", rowIndex, e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new BusinessException("Excel parsing failed", ErrorCodes.INVALID_REQUEST_FORMAT);
        }
        return pincodeRows;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue().trim();
                case NUMERIC -> String.valueOf(cell.getNumericCellValue());
                case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                default -> "";
            };
        } catch (Exception e) {
            log.warn("Failed to read cell value", e);
            return "";
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            BigDecimal bd = new BigDecimal(value);
            if (bd.compareTo(new BigDecimal("-180")) < 0 || bd.compareTo(new BigDecimal("180")) > 0) {
                log.warn("Invalid coordinate value: {}", value);
                return null;
            }
            return bd;
        } catch (Exception e) {
            log.warn("Failed to parse BigDecimal: {}", value);
            return null;
        }
    }

}
