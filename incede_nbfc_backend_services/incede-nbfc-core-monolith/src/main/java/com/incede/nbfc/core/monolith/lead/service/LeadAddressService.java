package com.incede.nbfc.core.monolith.lead.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerContactMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerContactRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import com.incede.nbfc.core.monolith.masterdata.repository.ContactTypesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service layer for managing Customer Contacts.
 * Handles creating, updating, retrieving, and soft-deleting contact details.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LeadAddressService {


}
