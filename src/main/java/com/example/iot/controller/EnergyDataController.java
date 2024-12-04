package com.example.iot.controller;

import com.example.iot.dto.EnergyReadingRequest;
import com.example.iot.dto.EnergyReadingResponse;
import com.example.iot.service.EnergyDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for managing energy consumption data from IoT devices.
 * Provides endpoints for recording and retrieving energy readings for specific devices.
 * 
 * <p>This controller handles all energy data-related HTTP requests with the base path "/api/devices/{deviceId}/readings".</p>
 * 
 * <p>Key functionalities include:
 * <ul>
 *   <li>Recording new energy readings</li>
 *   <li>Retrieving energy readings within a time range</li>
 *   <li>Getting latest energy readings</li>
 *   <li>Calculating average energy consumption</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.service.EnergyDataService
 * @see com.example.iot.dto.EnergyReadingRequest
 * @see com.example.iot.dto.EnergyReadingResponse
 */
@RestController
@RequestMapping("/api/devices/{deviceId}/readings")
@RequiredArgsConstructor
@Slf4j
public class EnergyDataController {

    private final EnergyDataService energyDataService;

    /**
     * Records a new energy reading for a specific device.
     *
     * @param deviceId the ID of the device submitting the reading
     * @param request the energy reading data
     * @return ResponseEntity containing the recorded reading information or not found status
     */
    @PostMapping
    public ResponseEntity<EnergyReadingResponse> recordReading(
            @PathVariable Long deviceId,
            @Valid @RequestBody EnergyReadingRequest request) {
        log.info("Recording energy reading for device ID: {}", deviceId);
        EnergyReadingResponse response = energyDataService.recordReading(deviceId, request);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    /**
     * Retrieves energy readings for a specific device within a time range.
     *
     * @param deviceId the ID of the device
     * @param startDate the start of the time range
     * @param endDate the end of the time range
     * @return ResponseEntity containing list of energy readings or not found status
     */
    @GetMapping
    public ResponseEntity<List<EnergyReadingResponse>> getReadings(
            @PathVariable Long deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Retrieving readings for device ID: {} between {} and {}", deviceId, startDate, endDate);
        List<EnergyReadingResponse> readings = energyDataService.getReadings(deviceId, startDate, endDate);
        return ResponseEntity.ok(readings);
    }

    /**
     * Retrieves the latest energy reading for a specific device.
     *
     * @param deviceId the ID of the device
     * @return ResponseEntity containing the latest reading or not found status
     */
    @GetMapping("/latest")
    public ResponseEntity<EnergyReadingResponse> getLatestReading(@PathVariable Long deviceId) {
        log.info("Retrieving latest reading for device ID: {}", deviceId);
        EnergyReadingResponse reading = energyDataService.getLatestReading(deviceId);
        return reading != null ? ResponseEntity.ok(reading) : ResponseEntity.notFound().build();
    }

    /**
     * Calculates the average energy consumption for a specific device.
     *
     * @param deviceId the ID of the device
     * @return ResponseEntity containing the average consumption value or not found status
     */
    @GetMapping("/average")
    public ResponseEntity<Double> getAverageConsumption(@PathVariable Long deviceId) {
        log.info("Calculating average consumption for device ID: {}", deviceId);
        Double average = energyDataService.getAverageConsumption(deviceId);
        return average != null ? ResponseEntity.ok(average) : ResponseEntity.notFound().build();
    }
}