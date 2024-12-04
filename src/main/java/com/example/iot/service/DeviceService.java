package com.example.iot.service;

import com.example.iot.repository.DeviceRepository;
import com.example.iot.dto.DeviceRegistrationRequest;
import com.example.iot.dto.DeviceResponse;
import com.example.iot.model.Device;
import com.example.iot.model.DeviceStatus;
import com.example.iot.dto.DeviceUpdateRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing IoT devices in the energy monitoring system.
 * Provides core business logic for device management operations.
 *
 * <p>This service handles:
 * <ul>
 *   <li>Device registration and validation</li>
 *   <li>Device information retrieval</li>
 *   <li>Device status management</li>
 *   <li>Device updates and deletion</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.repository.DeviceRepository
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /**
     * Registers a new device in the system.
     *
     * @param request Contains device registration details
     * @return DeviceResponse with the registered device information, null if serial number exists
     */
    @Transactional
    public DeviceResponse registerDevice(DeviceRegistrationRequest request) {
        log.info("Registering new device with serial number: {}", request.getSerialNumber());
        
        if (deviceRepository.existsBySerialNumber(request.getSerialNumber())) {
            log.warn("Device with serial number {} already exists", request.getSerialNumber());
            return null; 
        }
        
        Device device = Device.builder()
                .serialNumber(request.getSerialNumber())
                .deviceType(request.getDeviceType())
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(LocalDateTime.now())
                .build();
        
        Device savedDevice = deviceRepository.save(device);
        log.info("Device registered successfully with ID: {}", savedDevice.getId());
        return mapToDeviceResponse(savedDevice);
    }

    /**
     * Retrieves a specific device by its ID.
     *
     * @param id Device identifier
     * @return DeviceResponse if found, null otherwise
     */
    @Transactional
    public DeviceResponse getDeviceById (Long id) {
        log.info("Fetching device with ID : {}", id);
        return deviceRepository.findById(id)
            .map(this::mapToDeviceResponse)
            .orElse(null);
    }

    /**
     * Retrieves all registered devices.
     *
     * @return List of all devices in the system
     */
    @Transactional
    public List<DeviceResponse> getAllDevices() {
        log.info("Fetching all devices");
        return deviceRepository.findAll().stream()
            .map(this::mapToDeviceResponse)
            .collect(Collectors.toList());
    }

    /**
     * Updates the status of a device.
     *
     * @param id Device identifier
     * @param request New status to be set
     * @return Updated DeviceResponse if found, null otherwise
     */
    @Transactional
    public DeviceResponse updateDeviceStatus (Long id, DeviceStatus request) {
        log.info("Updating device status for ID : {}", id);

        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            log.warn("Device not found with ID : {}", id);
            return null;
        }

        device.setStatus(request);
        device.setLastReportTime(LocalDateTime.now());

        Device updatedDevice = deviceRepository.save(device);
        log.info("Device status updated successfully for ID : {}", id);
        return mapToDeviceResponse(updatedDevice);  
    }

    /**
     * Deletes a device from the system.
     *
     * @param id Device identifier
     * @return true if device was deleted, false if not found
     */
    @Transactional
    public boolean deleteDevice(Long id) {
        log.info("Attempting to delete device with ID : {}", id);

        if (!deviceRepository.existsById(id)) {
            log.warn("Device not found with ID : {}", id);
            return false;
        }

        deviceRepository.deleteById(id);
        log.info("Device deleted successfully with ID : {}", id);
        return true;
    } 

    /**
     * Retrieves devices by their operational status.
     *
     * @param status Status to filter by
     * @return List of devices matching the specified status
     */
    @Transactional
    public List<DeviceResponse> getDevicesByStatus(DeviceStatus status) {
        log.info("Fetching devices with status: {}", status);
        return deviceRepository.findByStatus(status).stream()
                .map(this::mapToDeviceResponse)
                .collect(Collectors.toList());
    }

    /**
     * Updates device information.
     *
     * @param id Device identifier
     * @param request Updated device information
     * @return Updated DeviceResponse if found and updated successfully, null otherwise
     */
    @Transactional
    public DeviceResponse updateDevice(Long id, DeviceUpdateRequest request) {
        log.info("Updating device with ID: {}", id);
        
        Device device = deviceRepository.findById(id).orElse(null);
        if (device == null) {
            log.warn("Device not found with ID: {}", id);
            return null;
        }

        device.setDeviceType(request.getDeviceType());
        if (request.getSerialNumber() != null && !request.getSerialNumber().equals(device.getSerialNumber())) {
            if (deviceRepository.existsBySerialNumber(request.getSerialNumber())) {
                log.warn("Device with serial number {} already exists", request.getSerialNumber());
                return null;
            }
            device.setSerialNumber(request.getSerialNumber());
        }

        Device updatedDevice = deviceRepository.save(device);
        log.info("Device updated successfully");
        return mapToDeviceResponse(updatedDevice);
    }

    /**
     * Converts a Device entity to a DeviceResponse DTO.
     *
     * @param device Device entity to convert
     * @return Mapped DeviceResponse object
     */
    private DeviceResponse mapToDeviceResponse(Device device) {
        return DeviceResponse.builder()
                .id(device.getId())
                .serialNumber(device.getSerialNumber())
                .deviceType(device.getDeviceType())
                .status(device.getStatus())
                .lastReportTime(device.getLastReportTime())
                .lastEnergyReading(device.getLastEnergyReading())
                .build();
    }

}
