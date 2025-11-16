package com.imatia.statemachine.domain.repository;

import com.imatia.statemachine.domain.model.OrderTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for OrderTracking entity.
 */
@Repository
public interface OrderTrackingRepository extends JpaRepository<OrderTracking, String> {

    /**
     * Obtain current OrderTracking by orderId
     * @param orderId Order identifier
     * @return current OrderTracking wrapped in an Optional
     */
    Optional<OrderTracking> findTopByOrderIdOrderByChangeStatusDateDesc(String orderId);

}
