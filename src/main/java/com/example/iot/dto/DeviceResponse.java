package com.example.iot.dto;

import com.example.iot.model.DeviceStatus;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for returning device information in API responses.
 * Provides a complete view of a device's current state and latest readings.
 *
 * <p>Includes:
 * <ul>
 *   <li>Device identification details</li>
 *   <li>Current operational status</li>
 *   <li>Latest energy reading information</li>
 *   <li>Timestamp of last report</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.model.DeviceStatus
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
  /**
   * Unique identifier of the device
   */
  private Long id;

  /**
   * Device's serial number
   */
  private String serialNumber;

  /**
   * Type or model of the device
   */
  private String deviceType;

  /**
   * Current operational status
   */
  private DeviceStatus status;

  /**
   * Timestamp of the last received report
   */
  private LocalDateTime lastReportTime;

  /**
   * Most recent energy consumption reading
   */
  private Double lastEnergyReading;
}
