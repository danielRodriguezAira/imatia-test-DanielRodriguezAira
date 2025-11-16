package com.example.tracking.domain.model;

import com.imatia.statemachine.domain.model.OrderTrackingStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for domain entities to verify they work correctly.
 */
class DomainModelTest {

    @Test
    void testPackageStatusEnum() {
        // Verify all enum values exist
        assertEquals(4, OrderTrackingStatus.values().length);
        assertNotNull(OrderTrackingStatus.PICKED_UP_IN_WAREHOUSE);
        assertNotNull(OrderTrackingStatus.OUT_FOR_DELIVERY);
        assertNotNull(OrderTrackingStatus.DELIVERY_INCIDENT);
        assertNotNull(OrderTrackingStatus.DELIVERED);
    }
}
