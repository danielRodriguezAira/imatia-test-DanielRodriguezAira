package com.imatia.statemachine.domain.validator;

import com.imatia.statemachine.domain.errors.OrderTrackingException;
import com.imatia.statemachine.domain.model.OrderTracking;
import com.imatia.statemachine.domain.model.OrderTrackingStatus;
import com.imatia.statemachine.domain.repository.OrderTrackingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateOrderTrackingValidator Tests")
class UpdateOrderTrackingValidatorImplTest {

    @Mock
    private OrderTrackingRepository orderTrackingRepository;

    @InjectMocks
    private UpdateOrderTrackingValidatorImpl validator;

    private OrderTracking newOrderTracking;
    private OrderTracking currentOrderTracking;

    @BeforeEach
    void setUp() {
        newOrderTracking = new OrderTracking();
        newOrderTracking.setOrderId("ORD-12345");
        newOrderTracking.setChangeStatusDate(new Date());

        currentOrderTracking = new OrderTracking();
        currentOrderTracking.setOrderId("ORD-12345");
        currentOrderTracking.setChangeStatusDate(new Date());
    }

    @Test
    @DisplayName("Should validate successfully when no current order tracking exists")
    void shouldValidateSuccessfully_WhenNoCurrentOrderTrackingExists() {
        // Arrange
        newOrderTracking.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);
        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(newOrderTracking));
        verify(orderTrackingRepository, times(1))
                .findTopByOrderIdOrderByChangeStatusDateDesc("ORD-12345");
    }

    @Test
    @DisplayName("Should validate successfully when transition is allowed from PICKED_UP_IN_WAREHOUSE to OUT_FOR_DELIVERY")
    void shouldValidateSuccessfully_WhenTransitionFromPickedUpToOutForDelivery() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);
        newOrderTracking.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(newOrderTracking));
        verify(orderTrackingRepository, times(1))
                .findTopByOrderIdOrderByChangeStatusDateDesc("ORD-12345");
    }

    @Test
    @DisplayName("Should validate successfully when transition is allowed from PICKED_UP_IN_WAREHOUSE to DELIVERY_INCIDENT")
    void shouldValidateSuccessfully_WhenTransitionFromPickedUpToDeliveryIncident() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);
        newOrderTracking.setStatus(OrderTrackingStatus.DELIVERY_INCIDENT);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(newOrderTracking));
    }

    @Test
    @DisplayName("Should validate successfully when transition is allowed from OUT_FOR_DELIVERY to DELIVERED")
    void shouldValidateSuccessfully_WhenTransitionFromOutForDeliveryToDelivered() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);
        newOrderTracking.setStatus(OrderTrackingStatus.DELIVERED);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(newOrderTracking));
    }

    @Test
    @DisplayName("Should validate successfully when transition is allowed from OUT_FOR_DELIVERY to DELIVERY_INCIDENT")
    void shouldValidateSuccessfully_WhenTransitionFromOutForDeliveryToDeliveryIncident() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);
        newOrderTracking.setStatus(OrderTrackingStatus.DELIVERY_INCIDENT);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(newOrderTracking));
    }

    @Test
    @DisplayName("Should validate successfully when transition is allowed from DELIVERY_INCIDENT to OUT_FOR_DELIVERY")
    void shouldValidateSuccessfully_WhenTransitionFromDeliveryIncidentToOutForDelivery() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.DELIVERY_INCIDENT);
        newOrderTracking.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(newOrderTracking));
    }

    @Test
    @DisplayName("Should validate successfully when transition is allowed from DELIVERY_INCIDENT to DELIVERED")
    void shouldValidateSuccessfully_WhenTransitionFromDeliveryIncidentToDelivered() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.DELIVERY_INCIDENT);
        newOrderTracking.setStatus(OrderTrackingStatus.DELIVERED);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(newOrderTracking));
    }

    @Test
    @DisplayName("Should throw exception when current status is final (DELIVERED)")
    void shouldThrowException_WhenCurrentStatusIsFinal() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.DELIVERED);
        newOrderTracking.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        OrderTrackingException exception = assertThrows(
                OrderTrackingException.class,
                () -> validator.validate(newOrderTracking)
        );

        assertEquals("203", exception.getCode());
        assertEquals("No transitions allowed from a final state", exception.getMessage());
        verify(orderTrackingRepository, times(1))
                .findTopByOrderIdOrderByChangeStatusDateDesc("ORD-12345");
    }

    @Test
    @DisplayName("Should throw exception when transition is invalid from PICKED_UP_IN_WAREHOUSE to DELIVERED")
    void shouldThrowException_WhenInvalidTransitionFromPickedUpToDelivered() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);
        newOrderTracking.setStatus(OrderTrackingStatus.DELIVERED);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        OrderTrackingException exception = assertThrows(
                OrderTrackingException.class,
                () -> validator.validate(newOrderTracking)
        );

        assertEquals("204", exception.getCode());
        assertTrue(exception.getMessage().contains("Invalid status transition"));
        assertTrue(exception.getMessage().contains("PICKED_UP_IN_WAREHOUSE"));
        assertTrue(exception.getMessage().contains("DELIVERED"));
    }

    @Test
    @DisplayName("Should throw exception when transition is invalid from OUT_FOR_DELIVERY to PICKED_UP_IN_WAREHOUSE")
    void shouldThrowException_WhenInvalidTransitionFromOutForDeliveryToPickedUp() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);
        newOrderTracking.setStatus(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        OrderTrackingException exception = assertThrows(
                OrderTrackingException.class,
                () -> validator.validate(newOrderTracking)
        );

        assertEquals("204", exception.getCode());
        assertTrue(exception.getMessage().contains("Invalid status transition"));
    }

    @Test
    @DisplayName("Should throw exception when trying to transition from DELIVERED to any status")
    void shouldThrowException_WhenTransitioningFromDeliveredToAnyStatus() {
        // Arrange
        currentOrderTracking.setStatus(OrderTrackingStatus.DELIVERED);
        newOrderTracking.setStatus(OrderTrackingStatus.DELIVERY_INCIDENT);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(anyString()))
                .thenReturn(Optional.of(currentOrderTracking));

        // Act & Assert
        OrderTrackingException exception = assertThrows(
                OrderTrackingException.class,
                () -> validator.validate(newOrderTracking)
        );

        assertEquals("203", exception.getCode());
        assertEquals("No transitions allowed from a final state", exception.getMessage());
    }

    @Test
    @DisplayName("Should call repository with correct orderId")
    void shouldCallRepositoryWithCorrectOrderId() {
        // Arrange
        String orderId = "TEST-ORDER-999";
        newOrderTracking.setOrderId(orderId);
        newOrderTracking.setStatus(OrderTrackingStatus.OUT_FOR_DELIVERY);

        when(orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(orderId))
                .thenReturn(Optional.empty());

        // Act
        validator.validate(newOrderTracking);

        // Assert
        verify(orderTrackingRepository, times(1))
                .findTopByOrderIdOrderByChangeStatusDateDesc(orderId);
    }
}