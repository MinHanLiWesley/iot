package com.example.iot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for handling device update requests.
 * Contains fields that can be modified after device registration.
 *
 * <p>Updatable fields:
 * <ul>
 *   <li>deviceType - New classification or model</li>
 *   <li>serialNumber - New serial number (optional)</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUpdateRequest {
    /**
     * New serial number for the device.
     * Optional field - only updated if provided.
     */
    private String serialNumber;

    /**
     * New device type or model.
     * Required field for device classification update.
     */
    @NotBlank(message = "Device type is required")
    private String deviceType;
}
