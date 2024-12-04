package com.example.iot.controller;

import com.example.iot.dto.DeviceRegistrationRequest;
import com.example.iot.dto.DeviceResponse;
import com.example.iot.dto.DeviceStatusUpdateRequest;
import com.example.iot.dto.DeviceUpdateRequest;
import com.example.iot.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing IoT devices in the energy monitoring system.
 * Provides endpoints for device registration, updates, status changes, and retrieval operations.
 * 
 * <p>This controller handles all device-related HTTP requests with the base path "/api/devices".</p>
 * 
 * <p>Key functionalities include:
 * <ul>
 *   <li>Device registration</li>
 *   <li>Device information retrieval</li>
 *   <li>Device status updates</li>
 *   <li>Device configuration modifications</li>
 *   <li>Device deletion</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.service.DeviceService
 * @see com.example.iot.dto.DeviceRegistrationRequest
 * @see com.example.iot.dto.DeviceResponse
 */
@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
@Slf4j
public class DeviceController {

    private final DeviceService deviceService;

    /**
     * Registers a new device in the system.
     *
     * @param request the device registration details
     * @return ResponseEntity containing the created device information or error status
     */
    @PostMapping
    public ResponseEntity<DeviceResponse> registerDevice(@Valid @RequestBody DeviceRegistrationRequest request) {
        log.info("Received device registration request for serial number: {}", request.getSerialNumber());
        DeviceResponse response = deviceService.registerDevice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all registered devices.
     *
     * @return ResponseEntity containing list of all devices
     */
    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAllDevices() {
        log.info("Retrieving all devices");
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    /**
     * Retrieves a specific device by its ID.
     *
     * @param id the device ID
     * @return ResponseEntity containing the device information or not found status
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable Long id) {
        log.info("Retrieving device with ID: {}", id);
        DeviceResponse device = deviceService.getDeviceById(id);
        return device != null ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }

    /**
     * Updates device information.
     *
     * @param id the device ID
     * @param request the updated device information
     * @return ResponseEntity containing the updated device information or not found status
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceUpdateRequest request) {
        log.info("Updating device with ID: {}", id);
        DeviceResponse updated = deviceService.updateDevice(id, request);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Updates device status.
     *
     * @param id the device ID
     * @param request the new device status
     * @return ResponseEntity containing the updated device information or not found status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<DeviceResponse> updateDeviceStatus(
            @PathVariable Long id,
            @Valid @RequestBody DeviceStatusUpdateRequest request) {
        log.info("Updating status for device with ID: {}", id);
        DeviceResponse updated = deviceService.updateDeviceStatus(id, request.getStatus());
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a device from the system.
     *
     * @param id the device ID
     * @return ResponseEntity with no content if successful, or not found status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.info("Deleting device with ID: {}", id);
        boolean deleted = deviceService.deleteDevice(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}