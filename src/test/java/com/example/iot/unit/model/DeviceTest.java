package com.example.iot.unit.model;

import com.example.iot.model.Device;
import com.example.iot.model.DeviceStatus;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.time.LocalDateTime;

/**
 * Unit test suite for the Device domain model.
 * 
 * <p>Comprehensive testing for Device entity, covering:
 * <ul>
 *   <li>Object creation and initialization</li>
 *   <li>Attribute manipulation</li>
 *   <li>State transitions</li>
 *   <li>Equality and hash code contract</li>
 *   <li>Builder pattern functionality</li>
 * </ul></p>
 * 
 * <p>Test Categories:
 * <ul>
 *   <li>Device object instantiation</li>
 *   <li>Attribute getter and setter methods</li>
 *   <li>Status change validation</li>
 *   <li>Equality comparison</li>
 *   <li>Nullable field handling</li>
 *   <li>Builder pattern verification</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see Device
 * @see DeviceStatus
 */
public class DeviceTest {
    
    private Device device1;
    private Device device2;
    private LocalDateTime testTime;

    @Before
    public void setUp() {
        testTime = LocalDateTime.now();
        
        device1 = new Device();
        device1.setId(1L);
        device1.setSerialNumber("TEST-001");
        device1.setDeviceType("SMART_METER");
        device1.setStatus(DeviceStatus.ACTIVE);
        device1.setLastReportTime(testTime);
        device1.setLastEnergyReading(100.0);
        
        device2 = new Device();
        device2.setId(1L);
        device2.setSerialNumber("TEST-001");
        device2.setDeviceType("SMART_METER");
        device2.setStatus(DeviceStatus.ACTIVE);
        device2.setLastReportTime(testTime);
        device2.setLastEnergyReading(100.0);
    }

    /**
     * Tests successful device object creation.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Device can be created with all attributes</li>
     *   <li>All getter methods return expected values</li>
     * </ul></p>
     */
    @Test
    public void testDeviceCreation() {
        assertNotNull("Device should not be null", device1);
        assertEquals("ID should be 1", Long.valueOf(1L), device1.getId());
        assertEquals("Serial number should match", "TEST-001", device1.getSerialNumber());
        assertEquals("Device type should match", "SMART_METER", device1.getDeviceType());
        assertEquals("Status should be ACTIVE", DeviceStatus.ACTIVE, device1.getStatus());
        assertEquals("Last report time should match", testTime, device1.getLastReportTime());
        assertEquals("Last energy reading should match", Double.valueOf(100.0), device1.getLastEnergyReading());
    }

    /**
     * Tests device object equality.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Devices with same attributes are considered equal</li>
     *   <li>Equality is symmetric</li>
     *   <li>Device is equal to itself</li>
     *   <li>Equal devices generate same hash code</li>
     * </ul></p>
     */
    @Test
    public void testDeviceEquality() {
        // Test equals method
        assertTrue("Devices should be equal", device1.equals(device2));
        assertTrue("Devices should be equal (symmetric)", device2.equals(device1));
        assertTrue("Device should be equal to itself", device1.equals(device1));
        
        // Test hashCode
        assertEquals("HashCodes should be equal", device1.hashCode(), device2.hashCode());
    }

    /**
     * Tests device object inequality.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Devices with different attributes are not equal</li>
     *   <li>Device is not equal to null</li>
     *   <li>Device is not equal to objects of different types</li>
     * </ul></p>
     */
    @Test
    public void testDeviceInequality() {
        Device differentDevice = new Device();
        differentDevice.setId(2L);
        differentDevice.setSerialNumber("TEST-002");
        
        assertFalse("Devices should not be equal", device1.equals(differentDevice));
        assertFalse("Device should not be equal to null", device1.equals(null));
        assertFalse("Device should not be equal to other type", device1.equals("TEST-001"));
    }

    /**
     * Tests device status transitions.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device status can be changed</li>
     *   <li>Status updates are correctly reflected</li>
     * </ul></p>
     */
    @Test
    public void testDeviceStatusTransitions() {
        device1.setStatus(DeviceStatus.INACTIVE);
        assertEquals("Status should be INACTIVE", DeviceStatus.INACTIVE, device1.getStatus());
        
        device1.setStatus(DeviceStatus.MAINTENANCE);
        assertEquals("Status should be MAINTENANCE", DeviceStatus.MAINTENANCE, device1.getStatus());
    }

    /**
     * Tests last report time update mechanism.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Last report time can be updated</li>
     *   <li>Updated time is correctly stored</li>
     * </ul></p>
     */
    @Test
    public void testLastReportTimeUpdate() {
        LocalDateTime newTime = LocalDateTime.now().plusHours(1);
        device1.setLastReportTime(newTime);
        assertEquals("Last report time should be updated", newTime, device1.getLastReportTime());
    }

    /**
     * Tests last energy reading update mechanism.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Last energy reading can be updated</li>
     *   <li>Updated reading is correctly stored</li>
     * </ul></p>
     */
    @Test
    public void testLastEnergyReadingUpdate() {
        device1.setLastEnergyReading(200.0);
        assertEquals("Last energy reading should be updated", Double.valueOf(200.0), device1.getLastEnergyReading());
    }

    /**
     * Tests device builder pattern.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Device can be created using builder</li>
     *   <li>All attributes are correctly set</li>
     * </ul></p>
     */
    @Test
    public void testDeviceBuilder() {
        Device builtDevice = Device.builder()
                .id(3L)
                .serialNumber("TEST-003")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(testTime)
                .lastEnergyReading(150.0)
                .build();

        assertNotNull("Built device should not be null", builtDevice);
        assertEquals("Built device ID should match", Long.valueOf(3L), builtDevice.getId());
        assertEquals("Built device serial number should match", "TEST-003", builtDevice.getSerialNumber());
        assertEquals("Built device energy reading should match", Double.valueOf(150.0), builtDevice.getLastEnergyReading());
    }

    /**
     * Tests device type update mechanism.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device type can be updated</li>
     *   <li>Updated type is correctly stored</li>
     * </ul></p>
     */
    @Test
    public void testDeviceTypeUpdate() {
        device1.setDeviceType("ENERGY_METER");
        assertEquals("Device type should be updated", "ENERGY_METER", device1.getDeviceType());
    }

    /**
     * Tests serial number update mechanism.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Serial number can be updated</li>
     *   <li>Updated serial number is correctly stored</li>
     * </ul></p>
     */
    @Test
    public void testSerialNumberUpdate() {
        device1.setSerialNumber("NEW-001");
        assertEquals("Serial number should be updated", "NEW-001", device1.getSerialNumber());
    }

    /**
     * Tests comprehensive device state change.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Multiple device attributes can be updated simultaneously</li>
     *   <li>All updates are correctly reflected</li>
     * </ul></p>
     */
    @Test
    public void testCompleteStateChange() {
        LocalDateTime newTime = LocalDateTime.now().plusHours(2);
        device1.setStatus(DeviceStatus.MAINTENANCE);
        device1.setLastReportTime(newTime);
        device1.setLastEnergyReading(300.0);
        
        assertEquals("Status should be MAINTENANCE", DeviceStatus.MAINTENANCE, device1.getStatus());
        assertEquals("Last report time should be updated", newTime, device1.getLastReportTime());
        assertEquals("Last energy reading should be updated", Double.valueOf(300.0), device1.getLastEnergyReading());
    }

    /**
     * Tests handling of nullable fields.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>New device has all fields initially null</li>
     *   <li>Nullable fields are handled correctly</li>
     * </ul></p>
     */
    @Test
    public void testNullableFields() {
        Device device = new Device();
        assertNull("New device should have null ID", device.getId());
        assertNull("New device should have null last report time", device.getLastReportTime());
        assertNull("New device should have null last energy reading", device.getLastEnergyReading());
    }

    /**
     * Tests device equality with different non-key fields.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Devices are equal based on key fields</li>
     *   <li>Non-key field differences do not affect equality</li>
     * </ul></p>
     */
    @Test
    public void testEqualityWithDifferentNonKeyFields() {
        Device device3 = new Device();
        device3.setId(1L);
        device3.setSerialNumber("TEST-001");
        device3.setDeviceType("DIFFERENT_TYPE");
        device3.setStatus(DeviceStatus.INACTIVE);
        device3.setLastEnergyReading(200.0);
        
        // Should still be equal because equality is based on ID and serialNumber
        assertTrue("Devices should be equal despite different non-key fields", device1.equals(device3));
        assertEquals("HashCodes should be equal despite different non-key fields", 
                    device1.hashCode(), device3.hashCode());
    }
}