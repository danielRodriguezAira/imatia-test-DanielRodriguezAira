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
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)  // ← AGREGAR ESTO
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
    @DisplayName("Should successfully update single order tracking")
    void shouldSuccessfullyUpdateSingleOrderTracking() {
        // Arrange
        request.getOrderTrackings().add(orderTrackingDTO);

        when(orderTrackingMapper.toDomain(orderTrackingDTO)).thenReturn(orderTracking);
        doNothing().when(updateOrderTrackingValidator).validate(orderTracking);
        when(orderTrackingRepository.save(orderTracking)).thenReturn(persistedOrderTracking);
        when(orderTrackingMapper.toDTO(persistedOrderTracking)).thenReturn(orderTrackingDTO);

        // Act
        UpdateOrderTrackingResponseDTO response = updateOrderTrackingStatus.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getResults().size());

        UpdateOrderTrackingResultDTO result = response.getResults().get(0);
        assertNotNull(result.getData());
        assertNull(result.getError());
        assertEquals("ORD-12345", result.getData().getOrderId());

        verify(orderTrackingMapper, times(1)).toDomain(orderTrackingDTO);
        verify(updateOrderTrackingValidator, times(1)).validate(orderTracking);
        verify(orderTrackingRepository, times(1)).save(orderTracking);
        verify(orderTrackingMapper, times(1)).toDTO(persistedOrderTracking);
    }

    @Test
    @DisplayName("Should successfully update multiple order trackings")
    void shouldSuccessfullyUpdateMultipleOrderTrackings() {
        // Arrange
        OrderTrackingDTO orderTrackingDTO2 = new OrderTrackingDTO();
        orderTrackingDTO2.setOrderId("ORD-67890");
        orderTrackingDTO2.setTrackingStatusId(2); // OUT_FOR_DELIVERY

        OrderTracking orderTracking2 = new OrderTracking();
        orderTracking2.setOrderId("ORD-67890");
        orderTracking2.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);

        OrderTracking persistedOrderTracking2 = new OrderTracking();
        persistedOrderTracking2.setId(2L);
        persistedOrderTracking2.setOrderId("ORD-67890");
        persistedOrderTracking2.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);

        request.setOrderTrackings(Arrays.asList(orderTrackingDTO, orderTrackingDTO2));

        when(orderTrackingMapper.toDomain(orderTrackingDTO)).thenReturn(orderTracking);
        when(orderTrackingMapper.toDomain(orderTrackingDTO2)).thenReturn(orderTracking2);
        doNothing().when(updateOrderTrackingValidator).validate(any());
        when(orderTrackingRepository.save(orderTracking)).thenReturn(persistedOrderTracking);
        when(orderTrackingRepository.save(orderTracking2)).thenReturn(persistedOrderTracking2);
        when(orderTrackingMapper.toDTO(persistedOrderTracking)).thenReturn(orderTrackingDTO);
        when(orderTrackingMapper.toDTO(persistedOrderTracking2)).thenReturn(orderTrackingDTO2);

        // Act
        UpdateOrderTrackingResponseDTO response = updateOrderTrackingStatus.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getResults().size());

        response.getResults().forEach(result -> {
            assertNotNull(result.getData());
            assertNull(result.getError());
        });

        verify(orderTrackingMapper, times(2)).toDomain(any());
        verify(updateOrderTrackingValidator, times(2)).validate(any());
        verify(orderTrackingRepository, times(2)).save(any());
        verify(orderTrackingMapper, times(2)).toDTO(any());
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
    @DisplayName("Should process all trackings independently when some fail")
    void shouldProcessAllTrackingsIndependently_WhenSomeFail() {
        // Arrange
        OrderTrackingDTO dto1 = new OrderTrackingDTO();
        dto1.setOrderId("ORD-001");
        dto1.setTrackingStatusId(999); // Will fail with invalid status

        OrderTrackingDTO dto2 = new OrderTrackingDTO();
        dto2.setOrderId("ORD-002");
        dto2.setTrackingStatusId(1); // Valid

        request.setOrderTrackings(Arrays.asList(dto1, dto2));

        OrderTracking tracking2 = new OrderTracking();
        tracking2.setOrderId("ORD-002");
        tracking2.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);

        OrderTracking persisted2 = new OrderTracking();
        persisted2.setId(2L);
        persisted2.setOrderId("ORD-002");
        persisted2.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);

        when(orderTrackingMapper.toDomain(dto2)).thenReturn(tracking2);
        doNothing().when(updateOrderTrackingValidator).validate(tracking2);
        when(orderTrackingRepository.save(tracking2)).thenReturn(persisted2);
        when(orderTrackingMapper.toDTO(persisted2)).thenReturn(dto2);

        // Act
        UpdateOrderTrackingResponseDTO response = updateOrderTrackingStatus.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals(3, response.getResults().size());

        // First failed
        assertNotNull(response.getResults().get(0).getError());
        assertEquals("201", response.getResults().get(0).getError().getCode());

        // Second succeeded
        assertNull(response.getResults().get(1).getError());

        verify(orderTrackingRepository, times(2)).save(any());
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