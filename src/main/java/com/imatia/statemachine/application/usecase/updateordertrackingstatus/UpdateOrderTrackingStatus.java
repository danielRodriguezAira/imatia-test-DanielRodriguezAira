package com.imatia.statemachine.application.usecase.updateordertrackingstatus;

import com.imatia.statemachine.application.dto.request.UpdateOrderTrackingRequestDTO;
import com.imatia.statemachine.application.dto.response.UpdateOrderTrackingResponseDTO;

/**
 * Use case for updating a status package.
 */
public interface UpdateOrderTrackingStatus {
    
    /**
     * Updates the status package in the system.
     * If the package doesn't exist, creates a new one.
     * @param request the package creation request
     * @return the created package response
     */
    UpdateOrderTrackingResponseDTO execute(UpdateOrderTrackingRequestDTO request);
}
