package com.incede.nbfc.core.monolith.lead.service;


import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUp;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryResponseDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadsFollowUpHistoryDto;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpHistoryRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.repository.FollowUpTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeadFollowUpHistoryServiceTest {

    @Mock
    private LeadFollowUpHistoryRepository leadFollowUpHistoryRepository;
    @Mock
    private LeadFollowUpRepository leadFollowUpRepository;
    @Mock
    private LeadRepository leadRepository;
    @Mock
    private FollowUpTypeRepository followUpTypeRepository;

    @InjectMocks
    private LeadFollowUpHistoryService service;

    private UUID leadIdentity;
    private UUID followUpIdentity;
    private UUID followUpTypeIdentity;
    private Lead lead;
    private LeadFollowUp followUp;
    private FollowUpType followUpType;

    @BeforeEach
    void setUp() {
        leadIdentity = UUID.randomUUID();
        followUpIdentity = UUID.randomUUID();
        followUpTypeIdentity = UUID.randomUUID();

        lead = new Lead();
        lead.setIdentity(leadIdentity);

        followUp = new LeadFollowUp();
        followUp.setIdentity(followUpIdentity);

        followUpType = new FollowUpType();
        followUpType.setIdentity(followUpTypeIdentity);
    }



    @Test
    void saveFollowUpHistory_LeadFollowUpNotFound() {
        LeadFollowUpHistoryRequestDto dto = new LeadFollowUpHistoryRequestDto();
        dto.setLeadFollowUpIdentity(followUpIdentity);

        when(leadFollowUpRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(followUpIdentity))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.saveFollowUpHistory(leadIdentity, dto));
    }

    @Test
    void saveFollowUpHistory_ExceptionWhileSaving() {
        LeadFollowUpHistoryRequestDto dto = new LeadFollowUpHistoryRequestDto();
        dto.setLeadFollowUpIdentity(followUpIdentity);
        dto.setFollowUpTypeIdentity(followUpTypeIdentity);

        when(leadFollowUpRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(followUpIdentity))
                .thenReturn(Optional.of(followUp));
        when(leadRepository.findByIdentityAndIsDelFalse(leadIdentity)).thenReturn(Optional.of(lead));
        when(followUpTypeRepository.findByIdentity(followUpTypeIdentity)).thenReturn(Optional.of(followUpType));

        when(leadFollowUpHistoryRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(BusinessException.class,
                () -> service.saveFollowUpHistory(leadIdentity, dto));
    }

    @Test
    void getFollowUpHistory_NoHistories() {
        when(leadRepository.findByIdentityAndIsDelFalse(leadIdentity)).thenReturn(Optional.of(lead));
        when(leadFollowUpHistoryRepository.findByLeadIdentityAndIsDelFalseOrderByFollowUpDateDesc(leadIdentity))
                .thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getFollowUpHistory(leadIdentity));
    }



    @Test
    void searchFollowUpHistory_NoResults() {
        Page<LeadFollowUpHistory> page = Page.empty();
        when(leadFollowUpHistoryRepository.searchFollowUpHistory(
                any(), any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        assertThrows(ResourceNotFoundException.class,
                () -> service.searchFollowUpHistory(
                        leadIdentity, followUpIdentity, 1, followUpTypeIdentity,
                        LocalDate.now(), LocalDate.now(), 0, 10));
    }
}
