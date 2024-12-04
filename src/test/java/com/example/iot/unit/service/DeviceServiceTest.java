package com.example.iot.unit.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.example.iot.repository.DeviceRepository;
import com.example.iot.dto.DeviceRegistrationRequest;
import com.example.iot.dto.DeviceResponse;
import com.example.iot.dto.DeviceUpdateRequest;
import com.example.iot.model.Device;
import com.example.iot.model.DeviceStatus;
import com.example.iot.service.DeviceService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Unit test suite for the DeviceService.
 * 
 * <p>Comprehensive testing for device service layer methods, covering:
 * <ul>
 *   <li>Device registration</li>
 *   <li>Device retrieval</li>
 *   <li>Device status updates</li>
 *   <li>Device deletion</li>
 *   <li>Device information updates</li>
 *   <li>Error and edge case handling</li>
 * </ul></p>
 * 
 * <p>Testing Strategies:
 * <ul>
 *   <li>Uses Mockito for mocking dependencies</li>
 *   <li>Validates service layer logic</li>
 *   <li>Checks both successful and failure scenarios</li>
 *   <li>Verifies interactions with repository</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see DeviceService
 * @see DeviceRepository
 */
@RunWith(MockitoJUnitRunner.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private Device testDevice;
    private DeviceRegistrationRequest registrationRequest;

    @Before
    public void setUp() {
        testDevice = new Device();
        testDevice.setId(1L);
        testDevice.setSerialNumber("TEST-001");
        testDevice.setDeviceType("SMART_METER");
        testDevice.setStatus(DeviceStatus.ACTIVE);
        testDevice.setLastReportTime(LocalDateTime.now());

        registrationRequest = new DeviceRegistrationRequest();
        registrationRequest.setSerialNumber("TEST-001");
        registrationRequest.setDeviceType("SMART_METER");
    }

    /**
     * Tests successful device registration.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Device can be registered with valid details</li>
     *   <li>Correct response is returned</li>
     *   <li>Device is saved to repository</li>
     * </ul></p>
     */
    @Test
    public void testRegisterDeviceSuccess() {
        when(deviceRepository.existsBySerialNumber("TEST-001")).thenReturn(false);
        when(deviceRepository.save(any(Device.class))).thenReturn(testDevice);

        DeviceResponse response = deviceService.registerDevice(registrationRequest);

        assertNotNull("Response should not be null", response);
        assertEquals("Serial number should match", "TEST-001", response.getSerialNumber());
        assertEquals("Device type should match", "SMART_METER", response.getDeviceType());
        assertEquals("Status should be ACTIVE", DeviceStatus.ACTIVE, response.getStatus());
        verify(deviceRepository).save(any(Device.class));
    }

    /**
     * Tests device registration with duplicate serial number.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Registration fails for duplicate serial number</li>
     *   <li>No device is saved</li>
     *   <li>Null response is returned</li>
     * </ul></p>
     */
    @Test
    public void testRegisterDeviceDuplicateSerialNumber() {
        when(deviceRepository.existsBySerialNumber("TEST-001")).thenReturn(true);

        DeviceResponse response = deviceService.registerDevice(registrationRequest);

        assertNull("Response should be null for duplicate serial number", response);
        verify(deviceRepository, never()).save(any(Device.class));
    }

    /**
     * Tests successful device retrieval by ID.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Device can be retrieved when ID exists</li>
     *   <li>Correct device details are returned</li>
     * </ul></p>
     */
    @Test
    public void testGetDeviceByIdSuccess() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        DeviceResponse response = deviceService.getDeviceById(1L);

        assertNotNull("Response should not be null", response);
        assertEquals("Serial number should match", "TEST-001", response.getSerialNumber());
    }

    /**
     * Tests device retrieval for non-existent ID.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Null is returned for non-existent device</li>
     *   <li>No exceptions are thrown</li>
     * </ul></p>
     */
    @Test
    public void testGetDeviceByIdNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        DeviceResponse response = deviceService.getDeviceById(1L);

        assertNull("Response should be null when device not found", response);
    }

    /**
     * Tests retrieval of all devices.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>All devices are retrieved</li>
     *   <li>Correct number of devices are returned</li>
     *   <li>Device details are accurate</li>
     * </ul></p>
     */
    @Test
    public void testGetAllDevices() {
        Device device2 = new Device();
        device2.setId(2L);
        device2.setSerialNumber("TEST-002");
        device2.setDeviceType("SMART_METER");
        device2.setStatus(DeviceStatus.ACTIVE);

        when(deviceRepository.findAll()).thenReturn(Arrays.asList(testDevice, device2));

        List<DeviceResponse> responses = deviceService.getAllDevices();

        assertNotNull("Response list should not be null", responses);
        assertEquals("Should return 2 devices", 2, responses.size());
        assertEquals("First device serial number should match", "TEST-001", responses.get(0).getSerialNumber());
        assertEquals("Second device serial number should match", "TEST-002", responses.get(1).getSerialNumber());
    }

    /**
     * Tests successful device status update.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device status can be changed</li>
     *   <li>Last report time is updated</li>
     *   <li>Updated device is saved</li>
     * </ul></p>
     */
    @Test
    public void testUpdateDeviceStatusSuccess() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(deviceRepository.save(any(Device.class))).thenReturn(testDevice);

        DeviceResponse response = deviceService.updateDeviceStatus(1L, DeviceStatus.MAINTENANCE);

        assertNotNull("Response should not be null", response);
        assertEquals("Status should be updated", DeviceStatus.MAINTENANCE, response.getStatus());
        assertNotNull("Last report time should be updated", response.getLastReportTime());
        verify(deviceRepository).save(any(Device.class));
    }

    /**
     * Tests device status update for non-existent device.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Null is returned for non-existent device</li>
     *   <li>No updates are performed</li>
     * </ul></p>
     */
    @Test
    public void testUpdateDeviceStatusNotFound() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        DeviceResponse response = deviceService.updateDeviceStatus(1L, DeviceStatus.MAINTENANCE);

        assertNull("Response should be null when device not found", response);
        verify(deviceRepository, never()).save(any(Device.class));
    }

    /**
     * Tests successful device deletion.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device can be deleted when it exists</li>
     *   <li>Deletion returns true</li>
     *   <li>Repository delete method is called</li>
     * </ul></p>
     */
    @Test
    public void testDeleteDeviceSuccess() {
        when(deviceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(deviceRepository).deleteById(1L);

        boolean result = deviceService.deleteDevice(1L);

        assertTrue("Delete should return true on success", result);
        verify(deviceRepository).deleteById(1L);
    }

    /**
     * Tests device deletion for non-existent device.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Deletion returns false for non-existent device</li>
     *   <li>No deletion attempt is made</li>
     * </ul></p>
     */
    @Test
    public void testDeleteDeviceNotFound() {
        when(deviceRepository.existsById(1L)).thenReturn(false);

        boolean result = deviceService.deleteDevice(1L);

        assertFalse("Delete should return false when device not found", result);
        verify(deviceRepository, never()).deleteById(anyLong());
    }

    /**
     * Tests retrieving devices by status.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Devices can be filtered by status</li>
     *   <li>Correct number of devices are returned</li>
     *   <li>Only devices with matching status are included</li>
     * </ul></p>
     */
    @Test
    public void testGetDevicesByStatus() {
        Device device2 = new Device();
        device2.setId(2L);
        device2.setSerialNumber("TEST-002");
        device2.setDeviceType("SMART_METER");
        device2.setStatus(DeviceStatus.MAINTENANCE);

        when(deviceRepository.findByStatus(DeviceStatus.ACTIVE))
                .thenReturn(Arrays.asList(testDevice));
        when(deviceRepository.findByStatus(DeviceStatus.MAINTENANCE))
                .thenReturn(Arrays.asList(device2));

        List<DeviceResponse> activeDevices = deviceService.getDevicesByStatus(DeviceStatus.ACTIVE);
        List<DeviceResponse> maintenanceDevices = deviceService.getDevicesByStatus(DeviceStatus.MAINTENANCE);

        assertEquals("Should find one active device", 1, activeDevices.size());
        assertEquals("Should find one maintenance device", 1, maintenanceDevices.size());
        assertEquals("Active device should match", "TEST-001", activeDevices.get(0).getSerialNumber());
        assertEquals("Maintenance device should match", "TEST-002", maintenanceDevices.get(0).getSerialNumber());
    }

    /**
     * Tests successful device information update.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Device details can be updated</li>
     *   <li>Updated device is saved</li>
     *   <li>Correct response is returned</li>
     * </ul></p>
     */
    @Test
    public void testUpdateDeviceSuccess() {
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest();
        updateRequest.setDeviceType("NEW_TYPE");
        updateRequest.setSerialNumber("TEST-002");

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(deviceRepository.existsBySerialNumber("TEST-002")).thenReturn(false);
        when(deviceRepository.save(any(Device.class))).thenReturn(testDevice);

        DeviceResponse response = deviceService.updateDevice(1L, updateRequest);

        assertNotNull("Response should not be null", response);
        assertEquals("Device type should be updated", "NEW_TYPE", response.getDeviceType());
        assertEquals("Serial number should be updated", "TEST-002", response.getSerialNumber());
        verify(deviceRepository).save(any(Device.class));
    }

    /**
     * Tests device update for non-existent device.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Null is returned for non-existent device</li>
     *   <li>No updates are performed</li>
     * </ul></p>
     */
    @Test
    public void testUpdateDeviceNotFound() {
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest();
        updateRequest.setDeviceType("NEW_TYPE");

        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        DeviceResponse response = deviceService.updateDevice(1L, updateRequest);

        assertNull("Response should be null when device not found", response);
        verify(deviceRepository, never()).save(any(Device.class));
    }

    /**
     * Tests device update with duplicate serial number.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Update fails for duplicate serial number</li>
     *   <li>No device is updated</li>
     *   <li>Null response is returned</li>
     * </ul></p>
     */
    @Test
    public void testUpdateDeviceDuplicateSerialNumber() {
        DeviceUpdateRequest updateRequest = new DeviceUpdateRequest();
        updateRequest.setDeviceType("NEW_TYPE");
        updateRequest.setSerialNumber("TEST-002");

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));
        when(deviceRepository.existsBySerialNumber("TEST-002")).thenReturn(true);

        DeviceResponse response = deviceService.updateDevice(1L, updateRequest);

        assertNull("Response should be null for duplicate serial number", response);
        verify(deviceRepository, never()).save(any(Device.class));
    }

}
