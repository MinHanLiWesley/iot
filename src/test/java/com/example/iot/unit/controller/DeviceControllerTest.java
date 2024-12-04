package com.example.iot.unit.controller;

import static org.mockito.ArgumentMatchers.any;
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

import com.example.iot.controller.DeviceController;
import com.example.iot.dto.DeviceRegistrationRequest;
import com.example.iot.dto.DeviceResponse;
import com.example.iot.dto.DeviceStatusUpdateRequest;
import com.example.iot.dto.DeviceUpdateRequest;
import com.example.iot.model.DeviceStatus;
import com.example.iot.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Unit test suite for the DeviceController.
 * 
 * <p>Comprehensive test coverage for device management REST API endpoints, 
 * focusing on validating controller behavior under various scenarios.</p>
 * 
 * <p>Test Categories:
 * <ul>
 *   <li>Device Registration</li>
 *   <li>Device Retrieval</li>
 *   <li>Device Status Updates</li>
 *   <li>Device Modification</li>
 *   <li>Device Deletion</li>
 *   <li>Error Handling</li>
 * </ul></p>
 * 
 * <p>Testing Strategies:
 * <ul>
 *   <li>Mocking DeviceService to isolate controller logic</li>
 *   <li>Using MockMvc for simulating HTTP requests</li>
 *   <li>Verifying HTTP responses and response content</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see DeviceController
 * @see DeviceService
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DeviceController.class)
public class DeviceControllerTest {

    /**
     * MockMvc instance for simulating HTTP requests to DeviceController.
     * Enables testing of REST endpoints without full server deployment.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked DeviceService to control service layer behavior during testing.
     * Allows precise control of method return values and interaction verification.
     */
    @MockBean
    private DeviceService deviceService;

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
     * Tests successful device registration.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Device can be registered with valid input</li>
     *   <li>HTTP status is CREATED (201)</li>
     *   <li>Returned device has correct properties</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testRegisterDevice() throws Exception {
        DeviceRegistrationRequest request = new DeviceRegistrationRequest();
        request.setSerialNumber("TEST-001");
        request.setDeviceType("SMART_METER");

        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(LocalDateTime.now())
                .build();

        when(deviceService.registerDevice(any(DeviceRegistrationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.serialNumber").value("TEST-001"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    /**
     * Tests device registration with duplicate serial number.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Duplicate device registration is rejected</li>
     *   <li>HTTP status is BAD REQUEST (400)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testRegisterDeviceDuplicate() throws Exception {
        DeviceRegistrationRequest request = new DeviceRegistrationRequest();
        request.setSerialNumber("TEST-001");
        request.setDeviceType("SMART_METER");

        when(deviceService.registerDevice(any(DeviceRegistrationRequest.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests successful device retrieval by ID.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device can be retrieved successfully</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Returned device has correct properties</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetDevice() throws Exception {
        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();

        when(deviceService.getDeviceById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.serialNumber").value("TEST-001"));
    }

    /**
     * Tests device retrieval for non-existent device.
     * 
     * <p>Validates error handling when:
     * <ul>
     *   <li>Requesting a device with an invalid ID</li>
     *   <li>HTTP status is NOT FOUND (404)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetDeviceNotFound() throws Exception {
        when(deviceService.getDeviceById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/devices/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests retrieval of all devices.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>All devices can be retrieved</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Correct number and properties of devices are returned</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testGetAllDevices() throws Exception {
        List<DeviceResponse> devices = Arrays.asList(
                DeviceResponse.builder()
                        .id(1L)
                        .serialNumber("TEST-001")
                        .status(DeviceStatus.ACTIVE)
                        .build(),
                DeviceResponse.builder()
                        .id(2L)
                        .serialNumber("TEST-002")
                        .status(DeviceStatus.INACTIVE)
                        .build()
        );

        when(deviceService.getAllDevices()).thenReturn(devices);

        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].status").value("INACTIVE"));
    }

    /**
     * Tests device status update.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device status can be updated successfully</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Updated device has the correct status</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testUpdateDeviceStatus() throws Exception {
        DeviceStatusUpdateRequest request = new DeviceStatusUpdateRequest();
        request.setStatus(DeviceStatus.MAINTENANCE);

        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .status(DeviceStatus.MAINTENANCE)
                .build();

        when(deviceService.updateDeviceStatus(1L, DeviceStatus.MAINTENANCE))
                .thenReturn(response);

        mockMvc.perform(put("/api/devices/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("MAINTENANCE"));
    }

    /**
     * Tests device information update.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Device information can be modified</li>
     *   <li>HTTP status is OK (200)</li>
     *   <li>Updated device has correct properties</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testUpdateDevice() throws Exception {
        DeviceUpdateRequest request = new DeviceUpdateRequest();
        request.setDeviceType("NEW_TYPE");
        request.setSerialNumber("TEST-002");

        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-002")
                .deviceType("NEW_TYPE")
                .status(DeviceStatus.ACTIVE)
                .build();

        when(deviceService.updateDevice(1L, request)).thenReturn(response);

        mockMvc.perform(put("/api/devices/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("TEST-002"))
                .andExpect(jsonPath("$.deviceType").value("NEW_TYPE"));
    }

    /**
     * Tests successful device deletion.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device can be deleted successfully</li>
     *   <li>HTTP status is NO CONTENT (204)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testDeleteDevice() throws Exception {
        when(deviceService.deleteDevice(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests device deletion for non-existent device.
     * 
     * <p>Validates error handling when:
     * <ul>
     *   <li>Attempting to delete a non-existent device</li>
     *   <li>HTTP status is NOT FOUND (404)</li>
     * </ul></p>
     * 
     * @throws Exception if an error occurs during request processing
     */
    @Test
    public void testDeleteDeviceNotFound() throws Exception {
        when(deviceService.deleteDevice(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/devices/1"))
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
        String invalidJson = "{\"serialNumber\": \"\"}"; // Empty serial number

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}