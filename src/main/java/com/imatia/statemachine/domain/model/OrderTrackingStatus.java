package com.imatia.statemachine.domain.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum OrderTrackingStatus {
    PICKED_UP_IN_WAREHOUSE(1,  true, false),
    DELIVERY_INCIDENT(3, false, false),
    OUT_FOR_DELIVERY(2, false, false),
    DELIVERED(4,  false, true);

    private final int id;
    private final boolean isInitial;
    private final boolean isFinal;

    OrderTrackingStatus(int id, boolean isInitial, boolean isFinal) {
        this.id = id;
        this.isInitial = isInitial;
        this.isFinal = isFinal;
    }

    public static OrderTrackingStatus fromId(int id) {
        for (OrderTrackingStatus status : OrderTrackingStatus.values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderTrackingStatus id: " + id);
    }

    public static boolean isValidId(int id) {
        for (OrderTrackingStatus status : OrderTrackingStatus.values()) {
            if (status.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean canTransitionTo(OrderTrackingStatus newStatus) {
        return getAllowedTransitionsFrom(this).contains(newStatus);
    }

    private List<OrderTrackingStatus> getAllowedTransitionsFrom(OrderTrackingStatus currentStatus) {
        switch (currentStatus) {
            case PICKED_UP_IN_WAREHOUSE:
                return Arrays.asList(OUT_FOR_DELIVERY, DELIVERY_INCIDENT);
            case OUT_FOR_DELIVERY:
                return Arrays.asList(DELIVERED, DELIVERY_INCIDENT);
            case DELIVERY_INCIDENT:
                return Arrays.asList(OUT_FOR_DELIVERY, DELIVERED);
            case DELIVERED:
                return new ArrayList<>();
            default:
                throw new IllegalArgumentException("Unknown OrderTrackingStatus: " + currentStatus);
        }
    }
}
