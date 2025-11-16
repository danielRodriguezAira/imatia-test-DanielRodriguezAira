package com.imatia.statemachine.application.mapper;

import com.imatia.statemachine.application.dto.request.OrderTrackingDTO;
import com.imatia.statemachine.domain.model.OrderTracking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderTrackingMapper {
    @Mapping(target = "status", expression = "java(com.imatia.statemachine.domain.model.OrderTrackingStatus.fromId(dto.getTrackingStatusId()))")
    OrderTracking toDomain(OrderTrackingDTO dto);

    @Mapping(target = "trackingStatusId", expression = "java(orderTracking.getStatus().getId())")
    OrderTrackingDTO toDTO(OrderTracking orderTracking);
}
