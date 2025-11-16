package com.imatia.statemachine.domain.validator;

import com.imatia.statemachine.domain.model.OrderTracking;

public interface UpdateOrderTrackingValidator {
    void validate(OrderTracking orderTracking);
}
