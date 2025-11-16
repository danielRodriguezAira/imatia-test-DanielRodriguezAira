package com.imatia.statemachine.application.dto.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * Request DTO for creating or updating a package.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "UpdateOrderTrackingRequest")
public class UpdateOrderTrackingRequestDTO {

    @Valid
    @JacksonXmlProperty(localName = "orderTrackings")
    private List<OrderTrackingDTO> orderTrackings;
}
