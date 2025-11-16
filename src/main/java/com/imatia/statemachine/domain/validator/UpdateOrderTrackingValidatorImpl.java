package com.imatia.statemachine.domain.validator;

import com.imatia.statemachine.domain.errors.OrderTrackingException;
import com.imatia.statemachine.domain.model.OrderTracking;
import com.imatia.statemachine.domain.model.OrderTrackingStatus;
import com.imatia.statemachine.domain.repository.OrderTrackingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateOrderTrackingValidatorImpl implements UpdateOrderTrackingValidator {

    private final OrderTrackingRepository orderTrackingRepository;

    public UpdateOrderTrackingValidatorImpl(OrderTrackingRepository orderTrackingRepository) {
        this.orderTrackingRepository = orderTrackingRepository;
    }

    @Override
    public void validate(OrderTracking orderTracking) {

        OrderTrackingStatus newStatus = orderTracking.getStatus();
        Optional<OrderTracking> optCurrentOrderTracking = orderTrackingRepository.findTopByOrderIdOrderByChangeStatusDateDesc(orderTracking.getOrderId());

        // No current order tracking, any status is valid
        if(!optCurrentOrderTracking.isPresent()) {
            return;
        }

        OrderTracking currentOrderTracking =  optCurrentOrderTracking.get();
        OrderTrackingStatus currentStatus = currentOrderTracking.getStatus();

        // No transitions allowed from a final state
        if(currentStatus.isFinal()) {
            throw new OrderTrackingException("203","No transitions allowed from a final state");
        }

        // If there is a current order tracking, check if the transition is valid
        if(!currentStatus.canTransitionTo(newStatus)) {
            throw new OrderTrackingException("204","Invalid status transition from " + currentStatus + " to " + newStatus);
        }
    }
}
