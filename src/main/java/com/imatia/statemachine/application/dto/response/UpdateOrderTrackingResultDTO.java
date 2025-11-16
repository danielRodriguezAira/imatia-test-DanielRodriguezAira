package com.imatia.statemachine.application.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.imatia.statemachine.application.dto.request.OrderTrackingDTO;
import com.imatia.statemachine.shared.Result;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JacksonXmlRootElement(localName = "UpdateOrderTrackingResult")
public class UpdateOrderTrackingResultDTO implements Result<OrderTrackingDTO, UpdateOrderTrackingErrorDTO> {
    @JacksonXmlProperty(localName = "data")
    private OrderTrackingDTO data;
    @JacksonXmlProperty(localName = "error")
    private UpdateOrderTrackingErrorDTO error;

    public UpdateOrderTrackingResultDTO(OrderTrackingDTO data) {
        this.data = data;
        this.error = null;
    }
}
