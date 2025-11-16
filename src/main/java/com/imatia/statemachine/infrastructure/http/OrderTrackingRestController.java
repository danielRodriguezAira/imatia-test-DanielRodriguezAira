package com.imatia.statemachine.infrastructure.http;

import com.imatia.statemachine.application.dto.request.UpdateOrderTrackingRequestDTO;
import com.imatia.statemachine.application.dto.response.UpdateOrderTrackingResponseDTO;
import com.imatia.statemachine.application.usecase.updateordertrackingstatus.UpdateOrderTrackingStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/order/tracking")
public class OrderTrackingRestController {

    private final UpdateOrderTrackingStatus updateOrderTrackingStatus;

    public OrderTrackingRestController(UpdateOrderTrackingStatus updateOrderTrackingStatus) {
        this.updateOrderTrackingStatus = updateOrderTrackingStatus;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public UpdateOrderTrackingResponseDTO updateOrderTracking(
            @RequestBody @Validated UpdateOrderTrackingRequestDTO request,
            BindingResult result) {
        if(result.hasErrors()) {
            log.error(result.getAllErrors());
            throw new IllegalArgumentException("Invalid request data");
        }
        return updateOrderTrackingStatus.execute(request);
    }
}
