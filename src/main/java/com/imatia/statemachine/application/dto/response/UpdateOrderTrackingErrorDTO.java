package com.imatia.statemachine.application.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateOrderTrackingErrorDTO {
    @JacksonXmlProperty(localName = "code")
    private String code;
    @JacksonXmlProperty(localName = "message")
    private String message;
}
