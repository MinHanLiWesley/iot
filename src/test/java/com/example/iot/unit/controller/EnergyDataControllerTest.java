package com.example.iot.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.iot.controller.EnergyDataController;
import com.example.iot.dto.EnergyReadingRequest;
import com.example.iot.dto.EnergyReadingResponse;
import com.example.iot.service.EnergyDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Unit test suite for the EnergyDataController.
 * 
 * <p>Comprehensive test coverage for energy reading management REST API endpoints, 
 * focusing on validating controller behavior under various scenarios.</p>
 * 
 * <p>Test Categories:
 * <ul>
 *   <li>Energy Reading Recording</li>
 *   <li>Energy Reading Retrieval</li>
 *   <li>Average Consumption Calculation</li>
 *   <li>Latest Reading Retrieval</li>
 *   <li>Error Handling</li>
 * </ul></p>
 * 
 * <p>Testing Strategies:
 * <ul>
 *   <li>Mocking EnergyDataService to isolate controller logic</li>
 *   <li>Using MockMvc for simulating HTTP requests</li>
 *   <li>Verifying HTTP responses and response content</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see EnergyDataController
 * @see EnergyDataService
 */
@RunWith(SpringRunner.class)
@WebMvcTest(EnergyDataController.class)
public class EnergyDataControllerTest {

    /**
     * MockMvc instance for simulating HTTP requests to EnergyDataController.
     * Enables testing of REST endpoints without full server deployment.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked EnergyDataService to control service layer behavior during testing.
     * Allows precise control of method return values and interaction verification.
     */
    @MockBean
    private EnergyDataService energyDataService;

    /**
     * ObjectMapper for JSON serialization/deserialization.
     * Configured with JavaTimeModule to handle Java 8 date/time types.
     */
    private ObjectMapper objectMapper;

    /**
     * Sets up test environment before each test method.
     * Initializes ObjectMapper with JavaTimeModule for proper date/time handling.
     */
    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Tests successful energy reading recording.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Energy reading can be recorded with valid input</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Returned reading has correct properties</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testRecordReading() throws Exception {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        EnergyReadingResponse response = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(1L)
                .deviceSerialNumber("TEST-001")
                .value(100.0)
                .timestamp(LocalDateTime.now())
                .build();

        when(energyDataService.recordReading(eq(1L), any(EnergyReadingRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/devices/1/readings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deviceId").value(1))
                .andExpect(jsonPath("$.value").value(100.0));
    }

    /**
     * Tests energy reading recording for non-existent device.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Reading cannot be recorded for non-existent device</li>
     *   <li>HTTP status is NOT FOUND (404)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testRecordReadingDeviceNotFound() throws Exception {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        when(energyDataService.recordReading(eq(1L), any(EnergyReadingRequest.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/devices/1/readings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests retrieval of energy readings within a time range.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Readings can be retrieved successfully</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Correct number and properties of readings are returned</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetReadings() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now();

        List<EnergyReadingResponse> readings = Arrays.asList(
                EnergyReadingResponse.builder()
                        .id(1L)
                        .deviceId(1L)
                        .value(100.0)
                        .timestamp(startTime.plusHours(1))
                        .build(),
                EnergyReadingResponse.builder()
                        .id(2L)
                        .deviceId(1L)
                        .value(150.0)
                        .timestamp(endTime)
                        .build()
        );

        when(energyDataService.getReadings(eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(readings);

        mockMvc.perform(get("/api/devices/1/readings")
                .param("startDate", startTime.toString())
                .param("endDate", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].value").value(100.0))
                .andExpect(jsonPath("$[1].value").value(150.0));
    }

    /**
     * Tests retrieval of average energy consumption.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Average consumption can be calculated</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Correct average value is returned</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetAverageConsumption() throws Exception {
        when(energyDataService.getAverageConsumption(1L)).thenReturn(125.0);

        mockMvc.perform(get("/api/devices/1/readings/average"))
                .andExpect(status().isOk())
                .andExpect(content().string("125.0"));
    }

    /**
     * Tests average consumption retrieval for non-existent device.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Average cannot be calculated for non-existent device</li>
     *   <li>HTTP status is NOT FOUND (404)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetAverageConsumptionDeviceNotFound() throws Exception {
        when(energyDataService.getAverageConsumption(1L)).thenReturn(null);

        mockMvc.perform(get("/api/devices/1/readings/average"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests retrieval of the latest energy reading.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Latest reading can be retrieved successfully</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Correct reading properties are returned</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetLatestReading() throws Exception {
        EnergyReadingResponse response = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(1L)
                .value(200.0)
                .timestamp(LocalDateTime.now())
                .build();

        when(energyDataService.getLatestReading(1L)).thenReturn(response);

        mockMvc.perform(get("/api/devices/1/readings/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.value").value(200.0));
    }

    /**
     * Tests latest reading retrieval for non-existent device.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Latest reading cannot be retrieved for non-existent device</li>
     *   <li>HTTP status is NOT FOUND (404)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetLatestReadingNotFound() throws Exception {
        when(energyDataService.getLatestReading(1L)).thenReturn(null);

        mockMvc.perform(get("/api/devices/1/readings/latest"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests handling of invalid request body.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Requests with invalid data are rejected</li>
     *   <li>HTTP status is BAD REQUEST (400)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testInvalidRequestBody() throws Exception {
        String invalidJson = "{\"value\": \"not a number\"}";

        mockMvc.perform(post("/api/devices/1/readings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}