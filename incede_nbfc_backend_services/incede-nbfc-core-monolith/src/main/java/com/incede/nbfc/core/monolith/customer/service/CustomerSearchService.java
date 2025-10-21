package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.CanvasserResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerSearchResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AgentMaster;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.CanvassedTypes;
import com.incede.nbfc.core.monolith.masterdata.repository.AgentMasterRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.CanvassedTypesRepository;
import com.incede.nbfc.core.monolith.service.VaultService;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Staff;
import com.incede.nbfc.core.monolith.tenant.repository.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CustomerSearchService {

    private final CustomerRepository customerRepository;
    private final LeadRepository leadRepository;
    private final VaultService vaultService;
    private final CanvassedTypesRepository canvassedTypesRepository;
    private final StaffRepository staffRepository;
    private final AgentMasterRepository agentMasterRepository;

    @Transactional
    public List<CustomerSearchResponseDto> searchCustomers(CustomerSearchRequestDto searchRequest) {
        Objects.requireNonNull(searchRequest, "Search request must not be null");
        log.info("Searching customers with criteria: {}", searchRequest);

        String aadhaarVaultRef = null;
        if (searchRequest.getAadhaarNumber() != null && !searchRequest.getAadhaarNumber().isBlank()) {
            try {
                FinaVaultResponseDto vaultResponse =
                        vaultService.generateVaultIdAndMaskAadhaar(searchRequest.getAadhaarNumber());
                if (vaultResponse != null) {
                    aadhaarVaultRef = vaultResponse.getUidReferenceKey();
                }
            } catch (Exception e) {
                log.error("Error while masking Aadhaar / fetching vault reference", e);
            }
        }

        List<Customer> customers = customerRepository.searchCustomersFlexible(
                searchRequest.getBranchCode(),
                searchRequest.getBranchId(),
                searchRequest.getMobileNumber() != null ? searchRequest.getMobileNumber().toString() : null,
                searchRequest.getEmailId(),
                searchRequest.getPanCard(),
                aadhaarVaultRef,
                searchRequest.getVoterId(),
                searchRequest.getPassportNumber(),
                searchRequest.getCustomerName()
        );

        log.info("Found {} customers with search criteria", customers.size());

        if (!customers.isEmpty()) {
            return customers.stream()
                    .map(this::mapToSearchResponseDto)
                    .collect(Collectors.toList());
        }

        boolean hasCustomerSpecificFields =
                (aadhaarVaultRef != null)
                        || (searchRequest.getPanCard() != null && !searchRequest.getPanCard().isBlank())
                        || (searchRequest.getVoterId() != null && !searchRequest.getVoterId().isBlank())
                        || (searchRequest.getPassportNumber() != null && !searchRequest.getPassportNumber().isBlank());

        if (hasCustomerSpecificFields) {
            log.info("Customer-specific identifiers present — skipping lead search.");
            return List.of();
        }

        String mobile = searchRequest.getMobileNumber() != null ? searchRequest.getMobileNumber().toString() : null;
        String email = searchRequest.getEmailId() != null && !searchRequest.getEmailId().isBlank()
                ? searchRequest.getEmailId().trim()
                : null;
        String name = searchRequest.getCustomerName() != null && !searchRequest.getCustomerName().isBlank()
                ? searchRequest.getCustomerName().trim()
                : null;

        List<Lead> leads = leadRepository.searchLeadDetails(mobile, email, name);
        log.info("Found {} leads matching criteria", leads.size());

        return leads.stream()
                .map(this::mapLeadToSearchResponseDto)
                .collect(Collectors.toList());
    }

    private CustomerSearchResponseDto mapToSearchResponseDto(Customer customer) {
        return CustomerSearchResponseDto.builder()
                .customerIdentity(customer.getIdentity())
                .isCustomerExist(true)
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .middleName(customer.getMiddleName())
                .lastName(customer.getLastName())
                .displayName(customer.getDisplayName())
                .mobile(customer.getMobileNumber())
                .branchCode(customer.getBranchId() != null ? customer.getBranchId().getBranchCode() : null)
                .build();
    }

    private CustomerSearchResponseDto mapLeadToSearchResponseDto(Lead lead) {
        return CustomerSearchResponseDto.builder()
                .customerCode(lead.getLeadCode())
                .isLeadExist(true)
                .firstName(lead.getFullName())
                .mobile(lead.getContactNumber())
                .build();
    }

    public List<CanvasserResponseDto> fetchCanvasserId(UUID canvassedTypeId,String canvasserName) {
        CanvassedTypes canvassedType = canvassedTypesRepository.findByIdentity(canvassedTypeId)
                .orElseThrow(() -> new BusinessException(CommonConstants.CANVASSED_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        List<CanvasserResponseDto> canvasserResponseDtoList = new ArrayList<>();

        if (Objects.equals(canvassedType.getName(), CommonConstants.STAFF)){
            List<Staff> staffList = staffRepository.findAllByStaffNameStartingWithIgnoreCase(canvasserName);

            for (Staff staff : staffList){
                CanvasserResponseDto canvasserResponseDto = new CanvasserResponseDto();
                canvasserResponseDto.setCanvasserName(staff.getStaffName());
                canvasserResponseDto.setCanvasserIdentity(staff.getIdentity());
                canvasserResponseDtoList.add(canvasserResponseDto);
            }
        } else if (Objects.equals(canvassedType.getName(), CommonConstants.AGENT)){
            List<AgentMaster> agentMasterList = agentMasterRepository.findAllByAgentNameStartingWithIgnoreCase(canvasserName);

            for(AgentMaster agentMaster : agentMasterList){
                CanvasserResponseDto canvasserResponseDto = new CanvasserResponseDto();
                canvasserResponseDto.setCanvasserName(agentMaster.getAgentName());
                canvasserResponseDto.setCanvasserIdentity(agentMaster.getIdentity());
                canvasserResponseDtoList.add(canvasserResponseDto);
            }
        } else if (Objects.equals(canvassedType.getName(), CommonConstants.CUSTOMER)) {
            List<Customer> customerList = customerRepository.findAllByFirstNameStartingWithIgnoreCase(canvasserName);

            for (Customer customer : customerList){
                CanvasserResponseDto canvasserResponseDto = new CanvasserResponseDto();
                canvasserResponseDto.setCanvasserName(customer.getFirstName() + " " + customer.getLastName());
                canvasserResponseDto.setCanvasserIdentity(customer.getIdentity());
                canvasserResponseDtoList.add(canvasserResponseDto);
            }
        }

        return canvasserResponseDtoList;
    }

    public List<CanvasserResponseDto> fetchCanvasserName(UUID canvassedTypeId, UUID canvasserIdentity) {
        CanvassedTypes canvassedType = canvassedTypesRepository.findByIdentity(canvassedTypeId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.CANVASSED_TYPE_NOT_FOUND,
                        ErrorCodes.RESOURCE_NOT_FOUND
                ));

        List<CanvasserResponseDto> responseList = new ArrayList<>();

        if (Objects.equals(canvassedType.getName(), CommonConstants.STAFF)) {
            Staff staff = staffRepository.findByIdentity(canvasserIdentity)
                    .orElseThrow(() -> new BusinessException(
                            CommonConstants.CANVASSER_NOT_FOUND,
                            ErrorCodes.RESOURCE_NOT_FOUND
                    ));
            CanvasserResponseDto dto = new CanvasserResponseDto();
            dto.setCanvasserName(staff.getStaffName());
            dto.setCanvasserIdentity(staff.getIdentity());
            responseList.add(dto);
        }

        else if (Objects.equals(canvassedType.getName(), CommonConstants.AGENT)) {
            AgentMaster agent = agentMasterRepository.findByIdentity(canvasserIdentity)
                    .orElseThrow(() -> new BusinessException(
                            CommonConstants.CANVASSER_NOT_FOUND,
                            ErrorCodes.RESOURCE_NOT_FOUND
                    ));
            CanvasserResponseDto dto = new CanvasserResponseDto();
            dto.setCanvasserName(agent.getAgentName());
            dto.setCanvasserIdentity(agent.getIdentity());
            responseList.add(dto);
        }

        else if (Objects.equals(canvassedType.getName(), CommonConstants.CUSTOMER)) {
            Customer customer = customerRepository.findByIdentity(canvasserIdentity)
                    .orElseThrow(() -> new BusinessException(
                            CommonConstants.CANVASSER_NOT_FOUND,
                            ErrorCodes.RESOURCE_NOT_FOUND
                    ));
            CanvasserResponseDto dto = new CanvasserResponseDto();
            dto.setCanvasserName(customer.getFirstName() + " " + customer.getLastName());
            dto.setCanvasserIdentity(customer.getIdentity());
            responseList.add(dto);
        }

        return responseList;
    }

}
