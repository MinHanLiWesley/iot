package com.example.iot.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for returning energy reading information in API responses.
 * Provides complete information about an energy consumption reading.
 *
 * <p>Includes:
 * <ul>
 *   <li>Reading identification</li>
 *   <li>Energy consumption value</li>
 *   <li>Timestamp information</li>
 *   <li>Associated device details</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyReadingResponse {
    /**
     * Unique identifier of the reading
     */
    private Long id;

    /**
     * ID of the device that provided the reading
     */
    private Long deviceId;

    /**
     * Serial number of the device
     */
    private String deviceSerialNumber;

    /**
     * Energy consumption value recorded
     */
    private Double value;

    /**
     * Timestamp when the reading was recorded
     */
    private LocalDateTime timestamp;
}
