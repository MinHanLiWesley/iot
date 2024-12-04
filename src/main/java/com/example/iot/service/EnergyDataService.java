package com.example.iot.service;

import com.example.iot.repository.EnergyDataRepository;
import com.example.iot.repository.DeviceRepository;
import com.example.iot.dto.EnergyReadingRequest;
import com.example.iot.dto.EnergyReadingResponse;
import com.example.iot.model.Device;
import com.example.iot.model.EnergyData;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing energy consumption data from IoT devices.
 * Handles the recording, retrieval, and analysis of energy readings from devices.
 *
 * <p>This service provides functionality for:
 * <ul>
 *   <li>Recording new energy readings from devices</li>
 *   <li>Retrieving historical energy consumption data</li>
 *   <li>Accessing latest device readings</li>
 *   <li>Calculating average energy consumption</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.repository.EnergyDataRepository
 * @see com.example.iot.repository.DeviceRepository
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EnergyDataService {
    
    private final EnergyDataRepository energyDataRepository;
    private final DeviceRepository deviceRepository;

    /**
     * Records a new energy reading for a specified device.
     * Updates the device's last reading and report time.
     *
     * @param deviceId ID of the device submitting the reading
     * @param request Contains the energy consumption value
     * @return EnergyReadingResponse with the recorded data, or null if device not found
     */
    @Transactional
    public EnergyReadingResponse recordReading(Long deviceId, EnergyReadingRequest request) {
        log.info("Recording energy reading for device ID: {}", deviceId);
        
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            log.warn("Device not found with ID: {}", deviceId);
            return null;
        }

        EnergyData energyData = new EnergyData();
        energyData.setDevice(device);
        energyData.setEnergyConsumed(request.getValue());
        energyData.setTimestamp(LocalDateTime.now());
        
        EnergyData savedReading = energyDataRepository.save(energyData);
        
        // Update device's last energy reading
        device.setLastEnergyReading(request.getValue());
        device.setLastReportTime(LocalDateTime.now());
        deviceRepository.save(device);
        
        log.info("Successfully recorded energy reading");
        return mapToEnergyReadingResponse(savedReading);
    }

    /**
     * Retrieves energy readings for a device within a specified time range.
     *
     * @param deviceId ID of the device
     * @param startTime Start of the time range
     * @param endTime End of the time range
     * @return List of energy readings within the time range
     */
    @Transactional
    public List<EnergyReadingResponse> getReadings(Long deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Fetching readings for device ID: {} between {} and {}", deviceId, startTime, endTime);
        
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            log.warn("Device not found with ID: {}", deviceId);
            return List.of();
        }

        return energyDataRepository.findByDeviceAndTimestampBetween(device, startTime, endTime)
                .stream()
                .map(this::mapToEnergyReadingResponse)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the average energy consumption for a device.
     *
     * @param deviceId ID of the device
     * @return Average consumption value, or null if device not found
     */
    @Transactional
    public Double getAverageConsumption(Long deviceId) {
        log.info("Calculating average consumption for device ID: {}", deviceId);
        
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            log.warn("Device not found with ID: {}", deviceId);
            return null;
        }

        return energyDataRepository.calculateAverageConsumption(device);
    }

    /**
     * Retrieves the most recent energy reading for a device.
     *
     * @param deviceId ID of the device
     * @return Latest energy reading, or null if device not found
     */
    @Transactional
    public EnergyReadingResponse getLatestReading(Long deviceId) {
        log.info("Fetching latest reading for device ID: {}", deviceId);
        
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) {
            log.warn("Device not found with ID: {}", deviceId);
            return null;
        }

        return energyDataRepository.findLatestReading(device)
                .map(this::mapToEnergyReadingResponse)
                .orElse(null);
    }

    /**
     * Converts an EnergyData entity to its DTO representation.
     *
     * @param energyData The energy data entity to convert
     * @return Corresponding EnergyReadingResponse DTO
     */
    private EnergyReadingResponse mapToEnergyReadingResponse(EnergyData energyData) {
        EnergyReadingResponse response = new EnergyReadingResponse();
        response.setId(energyData.getId());
        response.setDeviceId(energyData.getDevice().getId());
        response.setDeviceSerialNumber(energyData.getDevice().getSerialNumber());
        response.setValue(energyData.getEnergyConsumed());
        response.setTimestamp(energyData.getTimestamp());
        return response;
    }
}