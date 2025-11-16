package com.imatia.statemachine.application.usecase.updateordertrackingstatus;

import com.imatia.statemachine.application.dto.request.OrderTrackingDTO;
import com.imatia.statemachine.application.dto.request.UpdateOrderTrackingRequestDTO;
import com.imatia.statemachine.application.dto.response.UpdateOrderTrackingErrorDTO;
import com.imatia.statemachine.application.dto.response.UpdateOrderTrackingResponseDTO;
import com.imatia.statemachine.application.dto.response.UpdateOrderTrackingResultDTO;
import com.imatia.statemachine.application.mapper.OrderTrackingMapper;
import com.imatia.statemachine.domain.errors.OrderTrackingException;
import com.imatia.statemachine.domain.model.OrderTracking;
import com.imatia.statemachine.domain.model.OrderTrackingStatus;
import com.imatia.statemachine.domain.repository.OrderTrackingRepository;
import com.imatia.statemachine.domain.validator.UpdateOrderTrackingValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateOrderTrackingStatusImpl implements UpdateOrderTrackingStatus {

    private final UpdateOrderTrackingValidator updateOrderTrackingValidator;
    private final OrderTrackingRepository orderTrackingRepository;
    private final OrderTrackingMapper orderTrackingMapper;

    public UpdateOrderTrackingStatusImpl(UpdateOrderTrackingValidator updateOrderTrackingValidator, OrderTrackingRepository orderTrackingRepository, OrderTrackingMapper orderTrackingMapper) {
        this.updateOrderTrackingValidator = updateOrderTrackingValidator;
        this.orderTrackingRepository = orderTrackingRepository;
        this.orderTrackingMapper = orderTrackingMapper;
    }

    @Override
    public UpdateOrderTrackingResponseDTO execute(UpdateOrderTrackingRequestDTO request) {
        List<UpdateOrderTrackingResultDTO> results = new ArrayList<>();
        for (OrderTrackingDTO orderTrackingDTO : request.getOrderTrackings()) {
            // Validate tracking status id
            if (!OrderTrackingStatus.isValidId(orderTrackingDTO.getTrackingStatusId())) {
                results.add(new UpdateOrderTrackingResultDTO(orderTrackingDTO, new UpdateOrderTrackingErrorDTO("201", "Invalid status id")));
            }
            try {
                OrderTracking orderTracking = orderTrackingMapper.toDomain(orderTrackingDTO);
                updateOrderTrackingValidator.validate(orderTracking);
                OrderTracking persistedOrderTracking = orderTrackingRepository.saveAndFlush(orderTracking);
                OrderTrackingDTO orderTrackingDTOResult = orderTrackingMapper.toDTO(persistedOrderTracking);
                results.add(new UpdateOrderTrackingResultDTO(orderTrackingDTOResult));
            } catch (OrderTrackingException ex) {
                results.add(new UpdateOrderTrackingResultDTO(orderTrackingDTO, new UpdateOrderTrackingErrorDTO(ex.getCode(), ex.getMessage())));
            }
        }
        return new UpdateOrderTrackingResponseDTO(results);
    }
}
