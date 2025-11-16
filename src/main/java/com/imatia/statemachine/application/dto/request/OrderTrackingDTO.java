package com.imatia.statemachine.application.dto.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "OrderTracking")
public class OrderTrackingDTO {

    @NotBlank(message = "Order Id is required")
    @JacksonXmlProperty(localName = "orderId")
    private String orderId;

    @NotNull(message = "Tracking status is required")
    @JacksonXmlProperty(localName = "trackingStatusId")
    private Integer trackingStatusId;

    @NotNull(message = "Change status date is required")
    @JacksonXmlProperty(localName = "changeStatusDate")
    private Date changeStatusDate;

}
