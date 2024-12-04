package com.example.iot.model;

/**
 * Enumeration representing the operational status of IoT devices in the system.
 * Defines all possible states a device can be in at any given time.
 *
 * <p>Available statuses:
 * <ul>
 *   <li>ACTIVE - Device is operational and sending data</li>
 *   <li>INACTIVE - Device is powered off or not communicating</li>
 *   <li>MAINTENANCE - Device is undergoing maintenance or repairs</li>
 * </ul></p>
 *
 * <p>This enum is used in:
 * <ul>
 *   <li>Device entity status tracking</li>
 *   <li>Status update operations</li>
 *   <li>Device filtering and reporting</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.model.Device
 */
public enum DeviceStatus {
    /**
     * Device is operational and actively sending data
     */
    ACTIVE,
    
    /**
     * Device is powered off or not communicating with the system
     */
    INACTIVE,
    
    /**
     * Device is temporarily out of service for maintenance
     */
    MAINTENANCE
}
