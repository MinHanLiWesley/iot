package com.example.iot.model;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing an IoT device in the energy monitoring system.
 * This class maps to the 'devices' table in the database and contains all relevant
 * device information including identification, status, and latest readings.
 *
 * <p>Key features:
 * <ul>
 *   <li>Unique identification through ID and serial number</li>
 *   <li>Device type classification</li>
 *   <li>Operational status tracking</li>
 *   <li>Energy consumption monitoring</li>
 *   <li>Timestamp tracking for reports</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.model.DeviceStatus
 */
@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    /**
     * Unique identifier for the device.
     * Auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique serial number assigned to the device.
     * Used for device identification in business operations.
     */
    @Column(unique = true, nullable = false)
    private String serialNumber;

    /**
     * Type or model of the device.
     * Indicates the device's capabilities and purpose.
     */
    @Column(nullable = false)
    private String deviceType;

    /**
     * Current operational status of the device.
     * Stored as a string representation of DeviceStatus enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status;

    /**
     * Timestamp of the last data report received from the device.
     * Used to track device activity and communication.
     */
    @Column(name = "last_report_time")
    private LocalDateTime lastReportTime;

    /**
     * Most recent energy consumption reading from the device.
     * Cached value for quick access to latest reading.
     */
    @Column(name = "last_energy_reading")
    private Double lastEnergyReading;

    /**
     * Custom equals method for Device entity.
     * Two devices are considered equal if they have the same ID and serial number.
     * This is a business key based equality implementation which:
     * 1. Ensures consistent behavior with database identity
     * 2. Allows safe usage in collections (HashSet, HashMap)
     * 3. Avoids issues with circular references
     * 4. Maintains stability even when other fields change
     *
     * @param o The object to compare with
     * @return true if the devices are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        // Check if same instance
        if (this == o) return true;
        
        // Check if null or different class
        if (o == null || getClass() != o.getClass()) return false;
        
        // Cast to Device
        Device device = (Device) o;
        
        // Compare only ID and serialNumber
        // Both fields are used because:
        // - ID ensures database identity
        // - SerialNumber ensures business identity
        return Objects.equals(id, device.id) &&
               Objects.equals(serialNumber, device.serialNumber);
    }

    /**
     * Custom hashCode method for Device entity.
     * Generates hash code based on ID and serial number only.
     * This implementation:
     * 1. Matches the equals method (essential for Java collections)
     * 2. Remains consistent with database identity
     * 3. Provides good distribution for hash-based collections
     *
     * @return hash code value for the device
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber);
    }

    /**
     * Custom toString method for Device entity.
     * Provides a readable string representation of the device's essential information.
     *
     * @return String representation of the device
     */
    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", status=" + status +
                ", lastReportTime=" + lastReportTime +
                ", lastEnergyReading=" + lastEnergyReading +
                '}';
    }
}
