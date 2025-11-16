package com.imatia.statemachine.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Domain entity representing a package in the tracking system.
 * This is a pure domain model without any infrastructure concerns.
 */
@Entity
@Table(name = "order_tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "order_id", nullable = false)
    private String orderId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderTrackingStatus status;
    @Column(name = "change_status_date", nullable = false)
    private Date changeStatusDate;
    @Column(name = "creation_date", nullable = false)
    private Date creationDate = new Date();
}
