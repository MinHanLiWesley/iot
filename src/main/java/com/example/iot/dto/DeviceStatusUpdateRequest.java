package com.example.iot.dto;

import com.example.iot.model.DeviceStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO class for handling device status update requests.
 * Used to change the operational status of a device in the system.
 *
 * <p>This class validates that:
 * <ul>
 *   <li>A status value is always provided</li>
 *   <li>The status is a valid DeviceStatus enum value</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.model.DeviceStatus
 */
@Data
public class DeviceStatusUpdateRequest {
    /**
     * New status to be applied to the device.
     * Must be one of the defined DeviceStatus enum values.
     */
    @NotNull(message = "Status is required")
    private DeviceStatus status;
}
