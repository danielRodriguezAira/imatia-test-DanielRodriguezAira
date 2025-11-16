package com.imatia.statemachine.application.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for package information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "UpdateOrderTrackingResponse")
public class UpdateOrderTrackingResponseDTO {

    @JacksonXmlProperty(localName = "results")
    private List<UpdateOrderTrackingResultDTO> results;
}
