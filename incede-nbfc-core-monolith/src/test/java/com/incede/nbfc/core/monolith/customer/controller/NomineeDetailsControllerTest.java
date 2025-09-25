package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.service.NomineeDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NomineeDetailsControllerTest {

    @Mock
    private NomineeDetailsService nomineeDetailsService;

    @InjectMocks
    private NomineeDetailsController controller;

    private UUID customerId;
    private UUID nomineeId;
    private NomineeDetailsRequestDto requestDto;
    private NomineeDetailsResponseDto responseDto;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        nomineeId = UUID.randomUUID();

        requestDto = new NomineeDetailsRequestDto();
        requestDto.setFullName("Anu");
        requestDto.setRelationship(UUID.randomUUID());
        requestDto.setPercentageShare(null);

        responseDto = NomineeDetailsResponseDto.builder()
                .identity(customerId)
                .customerCode("CUST001")
                .status("SUCCESS")
                .build();
    }

    @Test
    void testCreateNominee() {
        when(nomineeDetailsService.createNominee(customerId, requestDto)).thenReturn(responseDto);

        ResponseEntity<NomineeDetailsResponseDto> response = controller.createNominee(customerId, requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(nomineeDetailsService, times(1)).createNominee(customerId, requestDto);
    }

    @Test
    void testUpdateNominee() {
        when(nomineeDetailsService.updateNominee(customerId, nomineeId, requestDto)).thenReturn(responseDto);

        ResponseEntity<NomineeDetailsResponseDto> response = controller.updateNominee(customerId, nomineeId, requestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(nomineeDetailsService, times(1)).updateNominee(customerId, nomineeId, requestDto);
    }
    @Test
    void testGetNomineesByCustomerIdentity() {
        when(nomineeDetailsService.getNomineesByCustomerIdentity(customerId))
                .thenReturn(responseDto);
        ResponseEntity<NomineeDetailsResponseDto> response =
                controller.getNomineesByCustomerIdentity(customerId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());

        verify(nomineeDetailsService, times(1))
                .getNomineesByCustomerIdentity(customerId);
    }


    @Test
    void testDeleteNominee() {
        doNothing().when(nomineeDetailsService).deleteNominee(customerId, nomineeId);

        ResponseEntity<NomineeDetailsResponseDto> response = controller.deleteNominee(customerId, nomineeId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(nomineeDetailsService, times(1)).deleteNominee(customerId, nomineeId);
    }
}
