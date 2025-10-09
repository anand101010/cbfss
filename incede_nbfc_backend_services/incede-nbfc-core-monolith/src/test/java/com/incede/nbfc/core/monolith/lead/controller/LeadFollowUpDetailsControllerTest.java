package com.incede.nbfc.core.monolith.lead.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.incede.nbfc.core.monolith.exception.GlobalExceptionHandler;
import com.incede.nbfc.core.monolith.lead.domain.entity.Lead;
import com.incede.nbfc.core.monolith.lead.domain.entity.LeadFollowUp;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsRequestDto;
import com.incede.nbfc.core.monolith.lead.dto.LeadFollowUpDetailsResponseDto;
import com.incede.nbfc.core.monolith.lead.repository.LeadFollowUpRepository;
import com.incede.nbfc.core.monolith.lead.repository.LeadRepository;
import com.incede.nbfc.core.monolith.lead.service.LeadFollowUpDetailsService;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.FollowUpType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Genders;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadSource;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStage;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.LeadStatus;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductService;
import com.incede.nbfc.core.monolith.masterdata.repository.FollowUpTypeRepository;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LeadFollowUpDetailsController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class LeadFollowUpDetailsControllerTest {
  @Autowired private GlobalExceptionHandler globalExceptionHandler;

  @Autowired private LeadFollowUpDetailsController leadFollowUpDetailsController;

  @InjectMocks private LeadFollowUpDetailsController leadFollowUpDetailsController2;

  @Mock private LeadFollowUpDetailsService leadFollowUpDetailsService;

  @MockBean private LeadFollowUpDetailsService leadFollowUpDetailsService2;

  /**
   * Test {@link LeadFollowUpDetailsController#createLeadFollowUp(UUID,
   * LeadFollowUpDetailsRequestDto)}.
   * LeadFollowUpDetailsRequestDto)}
   */
  @Test
  void testCreateLeadFollowUp() throws Exception {

    LeadFollowUpDetailsRequestDto leadFollowUpDetailsRequestDto =
        new LeadFollowUpDetailsRequestDto();
    leadFollowUpDetailsRequestDto.setFollowUpDate(LocalDate.of(1970, 1, 1));
    leadFollowUpDetailsRequestDto.setFollowUpNotes("Follow Up Notes");
    leadFollowUpDetailsRequestDto.setFollowUpTypeId(UUID.randomUUID());
    leadFollowUpDetailsRequestDto.setIsActive(true);
    leadFollowUpDetailsRequestDto.setNextFollowUpDate(LocalDate.of(1970, 1, 1));
    leadFollowUpDetailsRequestDto.setStaffId(1);
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule()); // <-- Add this
      String content = objectMapper.writeValueAsString(leadFollowUpDetailsRequestDto);

      MockHttpServletRequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(
                "/api/v1/leads/{leadIdentity}/follow-up-details", UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .content(content);
    MockMvcBuilders.standaloneSetup(leadFollowUpDetailsController)
        .setControllerAdvice(globalExceptionHandler)
        .build()
        .perform(requestBuilder);
  }

  /**
   * Test {@link LeadFollowUpDetailsController# createLeadFollowUp(UUID,
   * <p>Method under test: {@link LeadFollowUpDetailsController#createLeadFollowUp(UUID,
   * LeadFollowUpDetailsRequestDto)}
   */
  @Test
  void testCreateLeadFollowUpThenReturnBodyFollowUpDate() {

    FollowUpType followUpType = new FollowUpType();
    followUpType.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType.setCreatedBy(1);
    followUpType.setDescription("The characteristics of someone or something");
    followUpType.setFollowUpTypeId(1);
    followUpType.setIdentity(UUID.randomUUID());
    followUpType.setIsActive(true);
    followUpType.setIsDel(true);
    followUpType.setName("Name");
    followUpType.setSortOrder(1);
    followUpType.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType.setUpdatedBy(1);

    User assignToUser = new User();
    assignToUser.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    assignToUser.setCreatedBy(1);
    assignToUser.setIdentity(UUID.randomUUID());
    assignToUser.setIsDel(true);
    assignToUser.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    assignToUser.setUpdatedBy(1);
    assignToUser.setUserCode("User Code");
    assignToUser.setUserId(1);
    assignToUser.setUserName("janedoe");

    Genders gender = new Genders();
    gender.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    gender.setCreatedBy(1);
    gender.setGender("Gender");
    gender.setGenderId(1);
    gender.setIdentity(UUID.randomUUID());
    gender.setIsActive(true);
    gender.setIsDel(true);
    gender.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    gender.setUpdatedBy(1);

    LeadSource leadSource = new LeadSource();
    leadSource.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadSource.setCreatedBy(1);
    leadSource.setDescription("The characteristics of someone or something");
    leadSource.setIdentity(UUID.randomUUID());
    leadSource.setIsActive(true);
    leadSource.setIsDel(true);
    leadSource.setLeadSourceId(1);
    leadSource.setName("Name");
    leadSource.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadSource.setUpdatedBy(1);

    LeadStage leadStage = new LeadStage();
    leadStage.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStage.setCreatedBy(1);
    leadStage.setDescription("The characteristics of someone or something");
    leadStage.setIdentity(UUID.randomUUID());
    leadStage.setIsActive(true);
    leadStage.setIsDel(true);
    leadStage.setLeadStageId(1);
    leadStage.setName("Name");
    leadStage.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStage.setUpdatedBy(1);

    LeadStatus leadStatus = new LeadStatus();
    leadStatus.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStatus.setCreatedBy(1);
    leadStatus.setDescription("The characteristics of someone or something");
    leadStatus.setIdentity(UUID.randomUUID());
    leadStatus.setIsActive(true);
    leadStatus.setIsDel(true);
    leadStatus.setLeadStatusesId(1);
    leadStatus.setName("Name");
    leadStatus.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStatus.setUpdatedBy(1);

    ProductService productService = new ProductService();
    productService.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    productService.setCreatedBy(1);
    productService.setDescription("The characteristics of someone or something");
    productService.setIdentity(UUID.randomUUID());
    productService.setIsActive(true);
    productService.setIsDel(true);
    productService.setName("Name");
    productService.setProductServiceId(1);
    productService.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    productService.setUpdatedBy(1);

    Lead lead = new Lead();
    lead.setAssignToUser(assignToUser);
    lead.setContactNumber("42");
    lead.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    lead.setCreatedBy(1);
    lead.setCurrentAssignmentDate(LocalDate.of(1970, 1, 1));
    lead.setCurrentFollowUpDate(LocalDate.of(1970, 1, 1));
    lead.setCurrentFollowUpTypeId(1);
    lead.setCurrentStageDate(LocalDate.of(1970, 1, 1));
    lead.setEmail("jane.doe@example.org");
    lead.setFullName("Dr Jane Doe");
    lead.setGender(gender);
    lead.setIdentity(UUID.randomUUID());
    lead.setIsDel(true);
    lead.setLeadCode("Lead Code");
    lead.setLeadId(1);
    lead.setLeadSource(leadSource);
    lead.setLeadStage(leadStage);
    lead.setLeadStatus(leadStatus);
    lead.setProductService(productService);
    lead.setRemarks("Remarks");
    lead.setTenantId(1);
    lead.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    lead.setUpdatedBy(1);

    LeadFollowUp leadFollowUp = new LeadFollowUp();
    leadFollowUp.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadFollowUp.setCreatedBy(1);
    LocalDate followUpDate = LocalDate.of(1970, 1, 1);
    leadFollowUp.setFollowUpDate(followUpDate);
    leadFollowUp.setFollowUpId(1);
    leadFollowUp.setFollowUpNotes("Follow Up Notes");
    leadFollowUp.setFollowUpType(followUpType);
    UUID identity = UUID.randomUUID();
    leadFollowUp.setIdentity(identity);
    leadFollowUp.setIsActive(true);
    leadFollowUp.setIsDel(true);
    leadFollowUp.setLead(lead);
    LocalDate nextFollowUpDate = LocalDate.of(1970, 1, 1);
    leadFollowUp.setNextFollowUpDate(nextFollowUpDate);
    leadFollowUp.setStaffId(1);
    leadFollowUp.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadFollowUp.setUpdatedBy(1);
    LeadFollowUpRepository leadFollowUpRepository = mock(LeadFollowUpRepository.class);
    when(leadFollowUpRepository.save(Mockito.<LeadFollowUp>any())).thenReturn(leadFollowUp);
    LeadRepository leadRepository = mock(LeadRepository.class);
    Optional<Lead> ofResult = Optional.of(mock(Lead.class));
    when(leadRepository.findByIdentityAndIsDelFalse(Mockito.<UUID>any())).thenReturn(ofResult);

    FollowUpType followUpType2 = new FollowUpType();
    followUpType2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType2.setCreatedBy(1);
    followUpType2.setDescription("The characteristics of someone or something");
    followUpType2.setFollowUpTypeId(1);
    followUpType2.setIdentity(UUID.randomUUID());
    followUpType2.setIsActive(true);
    followUpType2.setIsDel(true);
    followUpType2.setName("Name");
    followUpType2.setSortOrder(1);
    followUpType2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType2.setUpdatedBy(1);
    Optional<FollowUpType> ofResult2 = Optional.of(followUpType2);
    FollowUpTypeRepository followUpTypeRepository = mock(FollowUpTypeRepository.class);
    when(followUpTypeRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult2);
    LeadFollowUpDetailsController leadFollowUpDetailsController =
        new LeadFollowUpDetailsController(
            new LeadFollowUpDetailsService(
                leadFollowUpRepository, leadRepository, followUpTypeRepository));
    UUID leadIdentity = UUID.randomUUID();


    ResponseEntity<LeadFollowUpDetailsResponseDto> actualCreateLeadFollowUpResult =
        leadFollowUpDetailsController.createLeadFollowUp(
            leadIdentity, new LeadFollowUpDetailsRequestDto());

    verify(leadRepository).findByIdentityAndIsDelFalse(isA(UUID.class));
    verify(followUpTypeRepository).findByIdentity(isNull());
    verify(leadFollowUpRepository).save(isA(LeadFollowUp.class));
    LeadFollowUpDetailsResponseDto body = actualCreateLeadFollowUpResult.getBody();
    LocalDate followUpDate2 = body.getFollowUpDate();
    assertEquals("1970-01-01", followUpDate2.toString());
    LocalDate nextFollowUpDate2 = body.getNextFollowUpDate();
    assertEquals("1970-01-01", nextFollowUpDate2.toString());
    assertEquals("Follow Up Notes", body.getFollowUpNotes());
    assertEquals("Follow-up Created", body.getMessage());
    assertEquals("Name", body.getFollowUpTypeName());
    assertEquals(1, body.getStaffId());
    assertSame(followUpDate, followUpDate2);
    assertSame(nextFollowUpDate, nextFollowUpDate2);
    assertSame(identity, body.getFollowUpIdentity());
  }

  /**
   * Test {@link LeadFollowUpDetailsController#createLeadFollowUp(UUID,
   * LeadFollowUpDetailsRequestDto)}
   */
  @Test
  void testCreateLeadFollowUpThenReturnBodyIsLeadFollowUpDetailsResponseDto() {

    LeadFollowUpDetailsService leadFollowUpService = mock(LeadFollowUpDetailsService.class);
    LeadFollowUpDetailsResponseDto leadFollowUpDetailsResponseDto =
        new LeadFollowUpDetailsResponseDto();
    when(leadFollowUpService.createFollowUp(
            Mockito.<UUID>any(), Mockito.<LeadFollowUpDetailsRequestDto>any()))
        .thenReturn(leadFollowUpDetailsResponseDto);
    LeadFollowUpDetailsController leadFollowUpDetailsController =
        new LeadFollowUpDetailsController(leadFollowUpService);
    UUID leadIdentity = UUID.randomUUID();

    ResponseEntity<LeadFollowUpDetailsResponseDto> actualCreateLeadFollowUpResult =
        leadFollowUpDetailsController.createLeadFollowUp(
            leadIdentity, new LeadFollowUpDetailsRequestDto());

    verify(leadFollowUpService)
        .createFollowUp(isA(UUID.class), isA(LeadFollowUpDetailsRequestDto.class));
    assertSame(leadFollowUpDetailsResponseDto, actualCreateLeadFollowUpResult.getBody());
  }

  /**
   * Test {@link LeadFollowUpDetailsController#updateLeadFollowUp(UUID, UUID,
   * LeadFollowUpDetailsRequestDto)}
   */
  @Test
  void testUpdateLeadFollowUp() {
        LeadFollowUp leadFollowUp = mock(LeadFollowUp.class);
    doNothing().when(leadFollowUp).setUpdatedBy(Mockito.<Integer>any());
    doNothing().when(leadFollowUp).setFollowUpDate(Mockito.<LocalDate>any());
    doNothing().when(leadFollowUp).setFollowUpNotes(Mockito.<String>any());
    doNothing().when(leadFollowUp).setFollowUpType(Mockito.<FollowUpType>any());
    doNothing().when(leadFollowUp).setIsActive(Mockito.<Boolean>any());
    doNothing().when(leadFollowUp).setLead(Mockito.<Lead>any());
    doNothing().when(leadFollowUp).setNextFollowUpDate(Mockito.<LocalDate>any());
    doNothing().when(leadFollowUp).setStaffId(anyInt());
    Optional<LeadFollowUp> ofResult = Optional.of(leadFollowUp);

    FollowUpType followUpType = new FollowUpType();
    followUpType.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType.setCreatedBy(1);
    followUpType.setDescription("The characteristics of someone or something");
    followUpType.setFollowUpTypeId(1);
    followUpType.setIdentity(UUID.randomUUID());
    followUpType.setIsActive(true);
    followUpType.setIsDel(true);
    followUpType.setName("Name");
    followUpType.setSortOrder(1);
    followUpType.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType.setUpdatedBy(1);

    User assignToUser = new User();
    assignToUser.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    assignToUser.setCreatedBy(1);
    assignToUser.setIdentity(UUID.randomUUID());
    assignToUser.setIsDel(true);
    assignToUser.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    assignToUser.setUpdatedBy(1);
    assignToUser.setUserCode("User Code");
    assignToUser.setUserId(1);
    assignToUser.setUserName("janedoe");

    Genders gender = new Genders();
    gender.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    gender.setCreatedBy(1);
    gender.setGender("Gender");
    gender.setGenderId(1);
    gender.setIdentity(UUID.randomUUID());
    gender.setIsActive(true);
    gender.setIsDel(true);
    gender.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    gender.setUpdatedBy(1);

    LeadSource leadSource = new LeadSource();
    leadSource.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadSource.setCreatedBy(1);
    leadSource.setDescription("The characteristics of someone or something");
    leadSource.setIdentity(UUID.randomUUID());
    leadSource.setIsActive(true);
    leadSource.setIsDel(true);
    leadSource.setLeadSourceId(1);
    leadSource.setName("Name");
    leadSource.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadSource.setUpdatedBy(1);

    LeadStage leadStage = new LeadStage();
    leadStage.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStage.setCreatedBy(1);
    leadStage.setDescription("The characteristics of someone or something");
    leadStage.setIdentity(UUID.randomUUID());
    leadStage.setIsActive(true);
    leadStage.setIsDel(true);
    leadStage.setLeadStageId(1);
    leadStage.setName("Name");
    leadStage.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStage.setUpdatedBy(1);

    LeadStatus leadStatus = new LeadStatus();
    leadStatus.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStatus.setCreatedBy(1);
    leadStatus.setDescription("The characteristics of someone or something");
    leadStatus.setIdentity(UUID.randomUUID());
    leadStatus.setIsActive(true);
    leadStatus.setIsDel(true);
    leadStatus.setLeadStatusesId(1);
    leadStatus.setName("Name");
    leadStatus.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadStatus.setUpdatedBy(1);

    ProductService productService = new ProductService();
    productService.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    productService.setCreatedBy(1);
    productService.setDescription("The characteristics of someone or something");
    productService.setIdentity(UUID.randomUUID());
    productService.setIsActive(true);
    productService.setIsDel(true);
    productService.setName("Name");
    productService.setProductServiceId(1);
    productService.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    productService.setUpdatedBy(1);

    Lead lead = new Lead();
    lead.setAssignToUser(assignToUser);
    lead.setContactNumber("42");
    lead.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    lead.setCreatedBy(1);
    lead.setCurrentAssignmentDate(LocalDate.of(1970, 1, 1));
    lead.setCurrentFollowUpDate(LocalDate.of(1970, 1, 1));
    lead.setCurrentFollowUpTypeId(1);
    lead.setCurrentStageDate(LocalDate.of(1970, 1, 1));
    lead.setEmail("jane.doe@example.org");
    lead.setFullName("Dr Jane Doe");
    lead.setGender(gender);
    lead.setIdentity(UUID.randomUUID());
    lead.setIsDel(true);
    lead.setLeadCode("Lead Code");
    lead.setLeadId(1);
    lead.setLeadSource(leadSource);
    lead.setLeadStage(leadStage);
    lead.setLeadStatus(leadStatus);
    lead.setProductService(productService);
    lead.setRemarks("Remarks");
    lead.setTenantId(1);
    lead.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    lead.setUpdatedBy(1);

    LeadFollowUp leadFollowUp2 = new LeadFollowUp();
    leadFollowUp2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadFollowUp2.setCreatedBy(1);
    LocalDate followUpDate = LocalDate.of(1970, 1, 1);
    leadFollowUp2.setFollowUpDate(followUpDate);
    leadFollowUp2.setFollowUpId(1);
    leadFollowUp2.setFollowUpNotes("Follow Up Notes");
    leadFollowUp2.setFollowUpType(followUpType);
    UUID identity = UUID.randomUUID();
    leadFollowUp2.setIdentity(identity);
    leadFollowUp2.setIsActive(true);
    leadFollowUp2.setIsDel(true);
    leadFollowUp2.setLead(lead);
    LocalDate nextFollowUpDate = LocalDate.of(1970, 1, 1);
    leadFollowUp2.setNextFollowUpDate(nextFollowUpDate);
    leadFollowUp2.setStaffId(1);
    leadFollowUp2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    leadFollowUp2.setUpdatedBy(1);
    LeadFollowUpRepository leadFollowUpRepository = mock(LeadFollowUpRepository.class);
    when(leadFollowUpRepository.save(Mockito.<LeadFollowUp>any())).thenReturn(leadFollowUp2);
    when(leadFollowUpRepository.findByIdentityAndIsDelFalseAndIsActiveTrue(Mockito.<UUID>any()))
        .thenReturn(ofResult);
    LeadRepository leadRepository = mock(LeadRepository.class);
    Optional<Lead> ofResult2 = Optional.of(mock(Lead.class));
    when(leadRepository.findByIdentityAndIsDelFalse(Mockito.<UUID>any())).thenReturn(ofResult2);

    FollowUpType followUpType2 = new FollowUpType();
    followUpType2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType2.setCreatedBy(1);
    followUpType2.setDescription("The characteristics of someone or something");
    followUpType2.setFollowUpTypeId(1);
    followUpType2.setIdentity(UUID.randomUUID());
    followUpType2.setIsActive(true);
    followUpType2.setIsDel(true);
    followUpType2.setName("Name");
    followUpType2.setSortOrder(1);
    followUpType2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
    followUpType2.setUpdatedBy(1);
    Optional<FollowUpType> ofResult3 = Optional.of(followUpType2);
    FollowUpTypeRepository followUpTypeRepository = mock(FollowUpTypeRepository.class);
    when(followUpTypeRepository.findByIdentity(Mockito.<UUID>any())).thenReturn(ofResult3);
    LeadFollowUpDetailsController leadFollowUpDetailsController =
        new LeadFollowUpDetailsController(
            new LeadFollowUpDetailsService(
                leadFollowUpRepository, leadRepository, followUpTypeRepository));
    UUID leadIdentity = UUID.randomUUID();
    UUID followUpId = UUID.randomUUID();


    ResponseEntity<LeadFollowUpDetailsResponseDto> actualUpdateLeadFollowUpResult =
        leadFollowUpDetailsController.updateLeadFollowUp(
            leadIdentity, followUpId, new LeadFollowUpDetailsRequestDto());

    verify(leadFollowUp).setUpdatedBy(1);
    verify(leadFollowUp).setFollowUpDate(isNull());
    verify(leadFollowUp).setFollowUpNotes(null);
    verify(leadFollowUp).setFollowUpType(isA(FollowUpType.class));
    verify(leadFollowUp).setIsActive(true);
    verify(leadFollowUp).setLead(isA(Lead.class));
    verify(leadFollowUp).setNextFollowUpDate(isNull());
    verify(leadFollowUp).setStaffId(0);
    verify(leadFollowUpRepository).findByIdentityAndIsDelFalseAndIsActiveTrue(isA(UUID.class));
    verify(leadRepository).findByIdentityAndIsDelFalse(isA(UUID.class));
    verify(followUpTypeRepository).findByIdentity(isNull());
    verify(leadFollowUpRepository).save(isA(LeadFollowUp.class));
    HttpStatusCode statusCode = actualUpdateLeadFollowUpResult.getStatusCode();
    assertTrue(statusCode instanceof HttpStatus);
    LeadFollowUpDetailsResponseDto body = actualUpdateLeadFollowUpResult.getBody();
    assertEquals("Follow Up Notes", body.getFollowUpNotes());
    assertEquals("Follow-up Created", body.getMessage());
    assertEquals("Name", body.getFollowUpTypeName());
    assertNull(body.getLeadIdentity());
    assertEquals(1, body.getStaffId());
    assertEquals(200, actualUpdateLeadFollowUpResult.getStatusCodeValue());
    assertEquals(HttpStatus.OK, statusCode);
    assertTrue(actualUpdateLeadFollowUpResult.hasBody());
    assertTrue(actualUpdateLeadFollowUpResult.getHeaders().isEmpty());
    assertSame(followUpDate, body.getFollowUpDate());
    assertSame(nextFollowUpDate, body.getNextFollowUpDate());
    assertSame(identity, body.getFollowUpIdentity());
  }




  /**
   * Test {@link LeadFollowUpDetailsController#getAllFollowUps(int, int)}.
   *
   * <p>Method under test: {@link LeadFollowUpDetailsController#getAllFollowUps(int, int)}
   */
  @Test
  void testGetAllFollowUps() throws Exception {

      lenient().when(leadFollowUpDetailsService.getAllActiveFollowUps(Mockito.<Pageable>any()))
              .thenReturn(new PageImpl<>(new ArrayList<>()));

      MockHttpServletRequestBuilder getResult =
        MockMvcRequestBuilders.get("/api/v1/leads/follow-up-details/getAll");
    MockHttpServletRequestBuilder paramResult = getResult.param("page", String.valueOf(1));
    MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1));


    MockMvcBuilders.standaloneSetup(leadFollowUpDetailsController2)
        .build()
        .perform(requestBuilder)
      ;
  }
}
