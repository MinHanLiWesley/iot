package com.example.iot.unit.model;

import com.example.iot.model.Device;
import com.example.iot.model.DeviceStatus;
import com.example.iot.model.EnergyData;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.time.LocalDateTime;

/**
 * Unit test suite for the EnergyData domain model.
 * 
 * <p>Comprehensive testing for EnergyData entity, covering:
 * <ul>
 *   <li>Object creation and initialization</li>
 *   <li>Attribute manipulation</li>
 *   <li>Device association</li>
 *   <li>Equality and hash code contract</li>
 *   <li>Builder pattern functionality</li>
 * </ul></p>
 * 
 * <p>Test Categories:
 * <ul>
 *   <li>Energy data object instantiation</li>
 *   <li>Attribute getter and setter methods</li>
 *   <li>Device relationship validation</li>
 *   <li>Equality comparison</li>
 *   <li>Nullable field handling</li>
 *   <li>Builder pattern verification</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see EnergyData
 * @see Device
 */
public class EnergyDataTest {
    
    private EnergyData energyData1;
    private EnergyData energyData2;
    private Device testDevice;
    private LocalDateTime testTime;

    @Before
    public void setUp() {
        testTime = LocalDateTime.now();
        
        // Create a test device
        testDevice = Device.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();
        
        // Create first energy data instance
        energyData1 = new EnergyData();
        energyData1.setId(1L);
        energyData1.setDevice(testDevice);
        energyData1.setEnergyConsumed(100.0);
        energyData1.setTimestamp(testTime);
        
        // Create second energy data instance with same values
        energyData2 = new EnergyData();
        energyData2.setId(1L);
        energyData2.setDevice(testDevice);
        energyData2.setEnergyConsumed(100.0);
        energyData2.setTimestamp(testTime);
    }

    /**
     * Tests successful energy data object creation.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>EnergyData can be created with all attributes</li>
     *   <li>All getter methods return expected values</li>
     * </ul></p>
     */
    @Test
    public void testEnergyDataCreation() {
        assertNotNull("EnergyData should not be null", energyData1);
        assertEquals("ID should be 1", Long.valueOf(1L), energyData1.getId());
        assertEquals("Device should match", testDevice, energyData1.getDevice());
        assertEquals("Energy consumed should be 100.0", Double.valueOf(100.0), energyData1.getEnergyConsumed());
        assertEquals("Timestamp should match", testTime, energyData1.getTimestamp());
    }

    /**
     * Tests energy data object equality.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Energy data with same attributes are considered equal</li>
     *   <li>Equality is symmetric</li>
     *   <li>Energy data is equal to itself</li>
     *   <li>Equal energy data generate same hash code</li>
     * </ul></p>
     */
    @Test
    public void testEnergyDataEquality() {
        // Test equals method
        assertTrue("Energy data instances should be equal", energyData1.equals(energyData2));
        assertTrue("Energy data instances should be equal (symmetric)", energyData2.equals(energyData1));
        assertTrue("Energy data should be equal to itself", energyData1.equals(energyData1));
        
        // Test hashCode
        assertEquals("HashCodes should be equal", energyData1.hashCode(), energyData2.hashCode());
    }

    /**
     * Tests energy data object inequality.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Energy data with different attributes are not equal</li>
     *   <li>Energy data is not equal to null</li>
     *   <li>Energy data is not equal to objects of different types</li>
     * </ul></p>
     */
    @Test
    public void testEnergyDataInequality() {
        EnergyData differentEnergyData = new EnergyData();
        differentEnergyData.setId(2L);
        differentEnergyData.setDevice(testDevice);
        differentEnergyData.setEnergyConsumed(200.0);
        differentEnergyData.setTimestamp(testTime);
        
        assertFalse("Energy data instances should not be equal", energyData1.equals(differentEnergyData));
        assertFalse("Energy data should not be equal to null", energyData1.equals(null));
        assertFalse("Energy data should not be equal to other type", energyData1.equals("TEST"));
    }

    /**
     * Tests energy value update mechanism.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Energy consumed can be updated</li>
     *   <li>Updated value is correctly stored</li>
     * </ul></p>
     */
    @Test
    public void testEnergyValueUpdate() {
        energyData1.setEnergyConsumed(150.0);
        assertEquals("Energy consumed should be updated", Double.valueOf(150.0), energyData1.getEnergyConsumed());
    }

    /**
     * Tests timestamp update mechanism.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Timestamp can be updated</li>
     *   <li>Updated timestamp is correctly stored</li>
     * </ul></p>
     */
    @Test
    public void testTimestampUpdate() {
        LocalDateTime newTime = LocalDateTime.now().plusHours(1);
        energyData1.setTimestamp(newTime);
        assertEquals("Timestamp should be updated", newTime, energyData1.getTimestamp());
    }

    /**
     * Tests device association update mechanism.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Device can be associated with energy data</li>
     *   <li>Device association can be changed</li>
     * </ul></p>
     */
    @Test
    public void testDeviceAssociation() {
        Device newDevice = Device.builder()
                .id(2L)
                .serialNumber("TEST-002")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();
        
        energyData1.setDevice(newDevice);
        assertEquals("Device should be updated", newDevice, energyData1.getDevice());
        assertNotEquals("Device should be different from original", testDevice, energyData1.getDevice());
    }

    /**
     * Tests handling of nullable fields.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>New energy data has all fields initially null</li>
     *   <li>Nullable fields are handled correctly</li>
     * </ul></p>
     */
    @Test
    public void testNullableFields() {
        EnergyData newEnergyData = new EnergyData();
        assertNull("New energy data should have null ID", newEnergyData.getId());
        assertNull("New energy data should have null device", newEnergyData.getDevice());
        assertNull("New energy data should have null timestamp", newEnergyData.getTimestamp());
    }

    /**
     * Tests energy data builder pattern.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Energy data can be created using builder</li>
     *   <li>All attributes are correctly set</li>
     * </ul></p>
     */
    @Test
    public void testEnergyDataBuilder() {
        EnergyData builtEnergyData = EnergyData.builder()
                .id(3L)
                .device(testDevice)
                .energyConsumed(200.0)
                .timestamp(testTime)
                .build();

        assertNotNull("Built energy data should not be null", builtEnergyData);
        assertEquals("Built energy data ID should match", Long.valueOf(3L), builtEnergyData.getId());
        assertEquals("Built energy data device should match", testDevice, builtEnergyData.getDevice());
        assertEquals("Built energy data energy consumed should match", Double.valueOf(200.0), builtEnergyData.getEnergyConsumed());
        assertEquals("Built energy data timestamp should match", testTime, builtEnergyData.getTimestamp());
    }
}
