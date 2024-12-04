package com.example.iot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for handling device registration requests.
 * Contains the necessary information required to register a new device in the system.
 *
 * <p>Required fields:
 * <ul>
 *   <li>serialNumber - Unique identifier for the device</li>
 *   <li>deviceType - Classification or model of the device</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRegistrationRequest {
    /**
     * Unique serial number for device identification.
     * Must be unique across all devices in the system.
     */
    @NotBlank(message = "Serial number is required")
    private String serialNumber;

    /**
     * Type or model of the device being registered.
     * Used for device classification and capability identification.
     */
    @NotBlank(message = "Device type is required")
    private String deviceType;
}
