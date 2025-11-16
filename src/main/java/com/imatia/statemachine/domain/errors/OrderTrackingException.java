package com.imatia.statemachine.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderTrackingException extends RuntimeException {
    private String code;
    public OrderTrackingException(String code, String message) {
        super(message);
        this.code = code;
    }
}
