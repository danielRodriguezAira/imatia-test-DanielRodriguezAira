package com.imatia.statemachine.application.usecase.updateordertrackingstatus;

import com.imatia.statemachine.application.dto.request.OrderTrackingDTO;
import com.imatia.statemachine.application.dto.request.UpdateOrderTrackingRequestDTO;
import com.imatia.statemachine.application.dto.response.UpdateOrderTrackingResponseDTO;
import com.imatia.statemachine.application.dto.response.UpdateOrderTrackingResultDTO;
import com.imatia.statemachine.application.mapper.OrderTrackingMapper;
import com.imatia.statemachine.domain.errors.OrderTrackingException;
import com.imatia.statemachine.domain.model.OrderTracking;
import com.imatia.statemachine.domain.model.OrderTrackingStatus;
import com.imatia.statemachine.domain.repository.OrderTrackingRepository;
import com.imatia.statemachine.domain.validator.UpdateOrderTrackingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("UpdateOrderTrackingStatus UseCase Tests")
class UpdateOrderTrackingStatusImplTest {

    @Mock
    private UpdateOrderTrackingValidator updateOrderTrackingValidator;

    @Mock
    private OrderTrackingRepository orderTrackingRepository;

    @Mock
    private OrderTrackingMapper orderTrackingMapper;

    @InjectMocks
    private UpdateOrderTrackingStatusImpl updateOrderTrackingStatus;

    private UpdateOrderTrackingRequestDTO request;
    private OrderTrackingDTO orderTrackingDTO;
    private OrderTracking orderTracking;
    private OrderTracking persistedOrderTracking;

    @BeforeEach
    void setUp() {
        orderTrackingDTO = new OrderTrackingDTO();
        orderTrackingDTO.setOrderId("ORD-12345");
        orderTrackingDTO.setTrackingStatusId(1); // PICKED_UP_IN_WAREHOUSE

        orderTracking = new OrderTracking();
        orderTracking.setOrderId("ORD-12345");
        orderTracking.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);
        orderTracking.setChangeStatusDate(new Date());

        persistedOrderTracking = new OrderTracking();
        persistedOrderTracking.setId(1L);
        persistedOrderTracking.setOrderId("ORD-12345");
        persistedOrderTracking.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);
        persistedOrderTracking.setChangeStatusDate(new Date());

        request = new UpdateOrderTrackingRequestDTO();
        request.setOrderTrackings(new ArrayList<>());
    }


    @Test
    @DisplayName("Should return error when validator throws OrderTrackingException")
    void shouldReturnError_WhenValidatorThrowsException() {
        // Arrange
        request.getOrderTrackings().add(orderTrackingDTO);

        when(orderTrackingMapper.toDomain(orderTrackingDTO)).thenReturn(orderTracking);
        doThrow(new OrderTrackingException("203", "No transitions allowed from a final state"))
                .when(updateOrderTrackingValidator).validate(orderTracking);

        // Act
        UpdateOrderTrackingResponseDTO response = updateOrderTrackingStatus.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());

        UpdateOrderTrackingResultDTO result = response.getResults().get(0);
        assertNotNull(result.getError());
        assertEquals("203", result.getError().getCode());
        assertEquals("No transitions allowed from a final state", result.getError().getMessage());
        assertNotNull(result.getData());

        verify(orderTrackingMapper, times(1)).toDomain(orderTrackingDTO);
        verify(updateOrderTrackingValidator, times(1)).validate(orderTracking);
        verify(orderTrackingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return error when invalid transition is attempted")
    void shouldReturnError_WhenInvalidTransitionAttempted() {
        // Arrange
        request.getOrderTrackings().add(orderTrackingDTO);

        when(orderTrackingMapper.toDomain(orderTrackingDTO)).thenReturn(orderTracking);
        doThrow(new OrderTrackingException("204", "Invalid status transition from DELIVERED to OUT_FOR_DELIVERY"))
                .when(updateOrderTrackingValidator).validate(orderTracking);

        // Act
        UpdateOrderTrackingResponseDTO response = updateOrderTrackingStatus.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());

        UpdateOrderTrackingResultDTO result = response.getResults().get(0);
        assertNotNull(result.getError());
        assertEquals("204", result.getError().getCode());
        assertTrue(result.getError().getMessage().contains("Invalid status transition"));

        verify(orderTrackingMapper, times(1)).toDomain(orderTrackingDTO);
        verify(updateOrderTrackingValidator, times(1)).validate(orderTracking);
        verify(orderTrackingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle empty request list")
    void shouldHandleEmptyRequestList() {
        // Arrange
        request.setOrderTrackings(new ArrayList<>());

        // Act
        UpdateOrderTrackingResponseDTO response = updateOrderTrackingStatus.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getResults().size());

        verify(orderTrackingMapper, never()).toDomain(any());
        verify(updateOrderTrackingValidator, never()).validate(any());
        verify(orderTrackingRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should preserve original DTO in error results")
    void shouldPreserveOriginalDTOInErrorResults() {
        // Arrange
        OrderTrackingDTO invalidDTO = new OrderTrackingDTO();
        invalidDTO.setOrderId("ORD-ERROR");
        invalidDTO.setTrackingStatusId(999); // Invalid
        request.getOrderTrackings().add(invalidDTO);

        // Act
        UpdateOrderTrackingResponseDTO response = updateOrderTrackingStatus.execute(request);

        // Assert
        UpdateOrderTrackingResultDTO result = response.getResults().get(0);
        assertSame(invalidDTO, result.getData());
        assertEquals("ORD-ERROR", result.getData().getOrderId());
        assertEquals(999, result.getData().getTrackingStatusId());
    }
}