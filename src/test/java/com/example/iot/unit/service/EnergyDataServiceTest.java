package com.example.iot.unit.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.iot.dto.EnergyReadingRequest;
import com.example.iot.dto.EnergyReadingResponse;
import com.example.iot.model.Device;
import com.example.iot.model.DeviceStatus;
import com.example.iot.model.EnergyData;
import com.example.iot.repository.DeviceRepository;
import com.example.iot.repository.EnergyDataRepository;
import com.example.iot.service.EnergyDataService;

/**
 * Unit test suite for the EnergyDataService.
 * 
 * <p>Comprehensive testing for energy data service layer methods, covering:
 * <ul>
 *   <li>Energy reading recording</li>
 *   <li>Energy reading retrieval</li>
 *   <li>Average energy consumption calculation</li>
 *   <li>Latest reading retrieval</li>
 *   <li>Error and edge case handling</li>
 * </ul></p>
 * 
 * <p>Testing Strategies:
 * <ul>
 *   <li>Uses Mockito for mocking dependencies</li>
 *   <li>Validates service layer logic</li>
 *   <li>Checks both successful and failure scenarios</li>
 *   <li>Verifies interactions with repositories</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see EnergyDataService
 * @see DeviceRepository
 * @see EnergyDataRepository
 */
@RunWith(MockitoJUnitRunner.class)
public class EnergyDataServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private EnergyDataRepository energyDataRepository;

    @InjectMocks
    private EnergyDataService energyDataService;

    private Device testDevice;
    private LocalDateTime testTime;

    @Before
    public void setUp() {
        testTime = LocalDateTime.now();
        testDevice = Device.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();
    }

    /**
     * Tests successful energy reading recording.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Reading can be recorded for an existing device</li>
     *   <li>Correct response is returned</li>
     *   <li>Device and reading are saved</li>
     * </ul></p>
     */
    @Test
    public void testRecordReading() {
        // Prepare test data
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(energyDataRepository.save(any(EnergyData.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        EnergyReadingResponse response = energyDataService.recordReading(1L, request);

        // Verify
        assertNotNull("Response should not be null", response);
        assertEquals("Device ID should match", Long.valueOf(1L), response.getDeviceId());
        assertEquals("Energy value should match", Double.valueOf(100.0), response.getValue());
        assertNotNull("Timestamp should not be null", response.getTimestamp());

        verify(deviceRepository).findById(1L);
        verify(energyDataRepository).save(any(EnergyData.class));
        verify(deviceRepository).save(testDevice);
    }

    /**
     * Tests energy reading recording for non-existent device.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>No reading is recorded for non-existent device</li>
     *   <li>Null response is returned</li>
     *   <li>No repository save operations occur</li>
     * </ul></p>
     */
    @Test
    public void testRecordReadingDeviceNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        EnergyReadingResponse response = energyDataService.recordReading(1L, request);
        assertNull("Response should be null for non-existent device", response);

        verify(deviceRepository).findById(1L);
        verify(energyDataRepository, never()).save(any());
    }

    /**
     * Tests retrieving energy readings within a time range.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Readings can be retrieved for an existing device</li>
     *   <li>Correct number of readings are returned</li>
     *   <li>Readings match the specified time range</li>
     * </ul></p>
     */
    @Test
    public void testGetReadings() {
        LocalDateTime startTime = testTime.minusHours(2);
        LocalDateTime endTime = testTime;

        EnergyData reading1 = EnergyData.builder()
                .id(1L)
                .device(testDevice)
                .energyConsumed(100.0)
                .timestamp(testTime.minusHours(1))
                .build();

        EnergyData reading2 = EnergyData.builder()
                .id(2L)
                .device(testDevice)
                .energyConsumed(150.0)
                .timestamp(testTime)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(energyDataRepository.findByDeviceAndTimestampBetween(testDevice, startTime, endTime))
                .thenReturn(Arrays.asList(reading1, reading2));

        List<EnergyReadingResponse> responses = energyDataService.getReadings(1L, startTime, endTime);

        assertNotNull("Response list should not be null", responses);
        assertEquals("Should return 2 readings", 2, responses.size());
        assertEquals("First reading value should match", Double.valueOf(100.0), responses.get(0).getValue());
        assertEquals("Second reading value should match", Double.valueOf(150.0), responses.get(1).getValue());
    }

    /**
     * Tests reading retrieval for non-existent device.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Empty list is returned for non-existent device</li>
     *   <li>No exceptions are thrown</li>
     * </ul></p>
     */
    @Test
    public void testGetReadingsDeviceNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        List<EnergyReadingResponse> responses = energyDataService.getReadings(
                1L, testTime.minusHours(2), testTime);

        assertNotNull("Response list should not be null", responses);
        assertTrue("Response list should be empty", responses.isEmpty());
    }

    /**
     * Tests calculation of average energy consumption.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Average can be calculated for an existing device</li>
     *   <li>Correct average value is returned</li>
     * </ul></p>
     */
    @Test
    public void testGetAverageConsumption() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(energyDataRepository.calculateAverageConsumption(testDevice)).thenReturn(125.0);

        Double average = energyDataService.getAverageConsumption(1L);

        assertNotNull("Average should not be null", average);
        assertEquals("Average should match", Double.valueOf(125.0), average);
    }

    /**
     * Tests average consumption calculation for non-existent device.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Null is returned for non-existent device</li>
     *   <li>No calculation is performed</li>
     * </ul></p>
     */
    @Test
    public void testGetAverageConsumptionDeviceNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        Double average = energyDataService.getAverageConsumption(1L);
        assertNull("Average should be null for non-existent device", average);
    }

    /**
     * Tests retrieval of the latest energy reading.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Latest reading can be retrieved for an existing device</li>
     *   <li>Correct reading details are returned</li>
     * </ul></p>
     */
    @Test
    public void testGetLatestReading() {
        EnergyData latestReading = EnergyData.builder()
                .id(1L)
                .device(testDevice)
                .energyConsumed(200.0)
                .timestamp(testTime)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(energyDataRepository.findLatestReading(testDevice)).thenReturn(Optional.of(latestReading));

        EnergyReadingResponse response = energyDataService.getLatestReading(1L);

        assertNotNull("Response should not be null", response);
        assertEquals("Energy value should match", Double.valueOf(200.0), response.getValue());
        assertEquals("Device ID should match", Long.valueOf(1L), response.getDeviceId());
    }

    /**
     * Tests latest reading retrieval for non-existent device.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Null is returned for non-existent device</li>
     *   <li>No retrieval attempt is made</li>
     * </ul></p>
     */
    @Test
    public void testGetLatestReadingDeviceNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        EnergyReadingResponse response = energyDataService.getLatestReading(1L);
        assertNull("Response should be null for non-existent device", response);
    }

    /**
     * Tests latest reading retrieval when no readings exist.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Null is returned when no readings are available</li>
     *   <li>Device existence is checked</li>
     * </ul></p>
     */
    @Test
    public void testGetLatestReadingNoReadings() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(energyDataRepository.findLatestReading(testDevice)).thenReturn(Optional.empty());

        EnergyReadingResponse response = energyDataService.getLatestReading(1L);
        assertNull("Response should be null when no readings exist", response);
    }
}