package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.service.LeadMasterDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LeadMasterDataControllerTest {

    @Mock
    private LeadMasterDataService leadMasterDataService;

    private LeadMasterDataController leadMasterDataController;

    private UUID tenantIdentity;

    @BeforeEach
    void setUp() {
        leadMasterDataService = mock(LeadMasterDataService.class);
        leadMasterDataController = new LeadMasterDataController(leadMasterDataService);
        tenantIdentity = UUID.randomUUID();
    }

    @Test
    void testGetAllAdditionalReferenceConfigs() {
        AdditionalReferenceConfigView mockConfig = mock(AdditionalReferenceConfigView.class);
        given(mockConfig.getReferenceFieldName()).willReturn("PAN");
        given(mockConfig.getReferenceFieldCode()).willReturn("PAN_CODE");
        given(mockConfig.getDataType()).willReturn("String");
        given(mockConfig.getSortOrder()).willReturn(1);
        given(mockConfig.getIsMandatory()).willReturn(true);
        given(mockConfig.getIsActive()).willReturn(true);
        given(mockConfig.getIdentity()).willReturn(UUID.randomUUID());

        given(leadMasterDataService.getAllAdditionalReferenceConfigs(tenantIdentity))
                .willReturn(List.of(mockConfig));

        ResponseEntity<List<AdditionalReferenceConfigView>> response =
                leadMasterDataController.getAllAdditionalReferenceConfigs(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getReferenceFieldName()).isEqualTo("PAN");
    }

    @Test
    void testGetAllLeadSources() {
        LeadSourceView mockSource = mock(LeadSourceView.class);
        given(mockSource.getName()).willReturn("Website");
        given(mockSource.getDescription()).willReturn("Lead from website");
        given(mockSource.getIsActive()).willReturn(true);

        given(leadMasterDataService.getAllLeadSources(tenantIdentity))
                .willReturn(List.of(mockSource));

        ResponseEntity<List<LeadSourceView>> response =
                leadMasterDataController.getAllLeadSources(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Website");
    }

    @Test
    void testGetAllLeadStages() {
        LeadStageView mockStage = mock(LeadStageView.class);
        given(mockStage.getName()).willReturn("Qualified");
        given(mockStage.getDescription()).willReturn("Lead is qualified");
        given(mockStage.getIsActive()).willReturn(true);

        given(leadMasterDataService.getAllLeadStages(tenantIdentity))
                .willReturn(List.of(mockStage));

        ResponseEntity<List<LeadStageView>> response =
                leadMasterDataController.getAllLeadStages(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Qualified");
    }

    @Test
    void testGetAllFollowUpTypes() {
        FollowUpTypeView mockType = mock(FollowUpTypeView.class);
        given(mockType.getName()).willReturn("Call");
        given(mockType.getDescription()).willReturn("Follow-up via call");
        given(mockType.getSortOrder()).willReturn(1);
        given(mockType.getIsActive()).willReturn(true);

        given(leadMasterDataService.getAllFollowUpTypes(tenantIdentity))
                .willReturn(List.of(mockType));

        ResponseEntity<List<FollowUpTypeView>> response =
                leadMasterDataController.getAllFollowUpTypes(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Call");
    }

    @Test
    void testGetAllLeadStatuses() {
        LeadStatusView mockStatus = mock(LeadStatusView.class);
        given(mockStatus.getName()).willReturn("Converted");
        given(mockStatus.getDescription()).willReturn("Lead converted to customer");
        given(mockStatus.getIsActive()).willReturn(true);

        given(leadMasterDataService.getAllLeadStatuses(tenantIdentity))
                .willReturn(List.of(mockStatus));

        ResponseEntity<List<LeadStatusView>> response =
                leadMasterDataController.getAllLeadStatuses(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Converted");
    }

    @Test
    void testGetAllProductServices() {
        ProductServiceView mockProduct = mock(ProductServiceView.class);
        given(mockProduct.getName()).willReturn("Loan Processing");
        given(mockProduct.getIsActive()).willReturn(true);

        given(leadMasterDataService.getAllProductServices(tenantIdentity))
                .willReturn(List.of(mockProduct));

        ResponseEntity<List<ProductServiceView>> response =
                leadMasterDataController.getAllProductServices(tenantIdentity);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Loan Processing");
        assertThat(response.getBody().get(0).getIsActive()).isTrue();
    }
}
