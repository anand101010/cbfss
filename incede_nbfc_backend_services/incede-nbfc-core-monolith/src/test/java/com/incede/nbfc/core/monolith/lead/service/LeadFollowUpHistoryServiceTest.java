package com.incede.nbfc.core.monolith.lead.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUpHistory;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStage;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpHistoryRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadsFollowUpHistoryDto;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpHistoryRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.FollowUpTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.LeadStageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class LeadFollowUpHistoryServiceTest {

    @InjectMocks
    private LeadFollowUpHistoryService service;

    @Mock
    private LeadFollowUpHistoryRepository leadFollowUpHistoryRepository;

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private FollowUpTypeRepository followUpTypeRepository;

    @Mock
    private LeadStageRepository leadStageRepository;

    private UUID leadId;
    private UUID leadStageId;
    private UUID followUpTypeId;
    private Lead lead;
    private LeadStage leadStage;
    private FollowUpType followUpType;
    private LeadFollowUpHistory history;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        leadId = UUID.randomUUID();
        leadStageId = UUID.randomUUID();
        followUpTypeId = UUID.randomUUID();

        lead = new Lead();
        lead.setIdentity(leadId);

        leadStage = new LeadStage();
        leadStage.setIdentity(leadStageId);

        followUpType = new FollowUpType();
        followUpType.setIdentity(followUpTypeId);

        history = new LeadFollowUpHistory();
        history.setIdentity(UUID.randomUUID());
        history.setLead(lead);
    }

    @Test
    void testSaveFollowUpHistory_Success() {
        LeadFollowUpHistoryRequestDto dto = createValidDto();

        when(leadRepository.findByIdentityAndIsDelFalse(leadId)).thenReturn(Optional.of(lead));
        when(leadStageRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(leadStageId)).thenReturn(Optional.of(leadStage));
        when(followUpTypeRepository.findByIdentity(followUpTypeId)).thenReturn(Optional.of(followUpType));
        when(leadFollowUpHistoryRepository.save(any())).thenReturn(history);

        LeadsFollowUpHistoryDto response = service.saveFollowUpHistory(leadId, dto);

        assertNotNull(response);
        assertEquals(leadId, response.getLeadIdentity());
        verify(leadFollowUpHistoryRepository, times(1)).save(any());
    }

    @Test
    void testSaveFollowUpHistory_LeadNotFound() {
        LeadFollowUpHistoryRequestDto dto = createValidDto();
        when(leadRepository.findByIdentityAndIsDelFalse(leadId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.saveFollowUpHistory(leadId, dto));
    }

    @Test
    void testUpdateFollowUpHistory_Success() {
        LeadFollowUpHistoryRequestDto dto = createValidDto();
        UUID followUpHistoryId = UUID.randomUUID();

        when(leadRepository.findByIdentityAndIsDelFalse(leadId)).thenReturn(Optional.of(lead));
        when(leadFollowUpHistoryRepository.findByIdentityAndIsDelFalse(followUpHistoryId)).thenReturn(Optional.of(history));
        when(leadStageRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(leadStageId)).thenReturn(Optional.of(leadStage));
        when(followUpTypeRepository.findByIdentity(followUpTypeId)).thenReturn(Optional.of(followUpType));
        when(leadFollowUpHistoryRepository.save(any())).thenReturn(history);

        LeadsFollowUpHistoryDto response = service.updateFollowUpHistory(leadId, followUpHistoryId, dto);

        assertNotNull(response);
        verify(leadFollowUpHistoryRepository, times(1)).save(any());
    }

    @Test
    void testBulkSaveFollowUpHistory_Success() {
        LeadFollowUpHistoryRequestDto dto1 = createValidDto();
        LeadFollowUpHistoryRequestDto dto2 = createValidDto();

        when(leadRepository.findByIdentityAndIsDelFalse(any())).thenReturn(Optional.of(lead));
        when(leadStageRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(any())).thenReturn(Optional.of(leadStage));
        when(followUpTypeRepository.findByIdentity(any())).thenReturn(Optional.of(followUpType));
        when(leadFollowUpHistoryRepository.save(any())).thenReturn(history);

        List<LeadsFollowUpHistoryDto> result = service.bulkSaveFollowUpHistory(List.of(dto1, dto2));

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(leadFollowUpHistoryRepository, times(2)).save(any());
    }


    private LeadFollowUpHistoryRequestDto createValidDto() {
        LeadFollowUpHistoryRequestDto dto = new LeadFollowUpHistoryRequestDto();
        dto.setLeadIdentity(leadId);
        dto.setLeadStageIdentity(leadStageId);
        dto.setStaffId(123);
        dto.setFollowUpTypeIdentity(followUpTypeId);
        dto.setFollowUpDate(LocalDate.now().plusDays(1));
        dto.setNextFollowUpDate(LocalDate.now().plusDays(5));
        dto.setFollowUpNotes("Test note");
        dto.setChangeType("UPDATE");
        dto.setStageChangeRemarks("Stage changed");
        return dto;
    }
}
