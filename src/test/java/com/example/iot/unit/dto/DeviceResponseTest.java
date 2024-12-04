package com.example.iot.unit.dto;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import com.example.iot.dto.DeviceResponse;
import com.example.iot.model.DeviceStatus;

/**
 * Unit test suite for the DeviceResponse DTO.
 * 
 * <p>Comprehensive testing for device response data transfer object.
 * Focuses on validating:
 * <ul>
 *   <li>Builder pattern functionality</li>
 *   <li>Constructors</li>
 *   <li>Getter and setter methods</li>
 *   <li>Equals and hashCode contract</li>
 *   <li>toString method</li>
 *   <li>Object creation and copying</li>
 * </ul></p>
 * 
 * <p>Test Categories:
 * <ul>
 *   <li>Object creation with all fields</li>
 *   <li>Handling of nullable fields</li>
 *   <li>No-args constructor behavior</li>
 *   <li>Setter and getter method validation</li>
 *   <li>Object equality and hash code generation</li>
 *   <li>String representation testing</li>
 *   <li>Object copying</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see DeviceResponse
 */
public class DeviceResponseTest {

    /**
     * Tests builder pattern with all fields populated.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Builder creates object with all fields correctly</li>
     *   <li>All getter methods return expected values</li>
     * </ul></p>
     */
    @Test
    public void testBuilderWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(now)
                .lastEnergyReading(100.0)
                .build();

        assertNotNull("Response should not be null", response);
        assertEquals("ID should match", Long.valueOf(1L), response.getId());
        assertEquals("Serial number should match", "TEST-001", response.getSerialNumber());
        assertEquals("Device type should match", "SMART_METER", response.getDeviceType());
        assertEquals("Status should match", DeviceStatus.ACTIVE, response.getStatus());
        assertEquals("Last report time should match", now, response.getLastReportTime());
        assertEquals("Last energy reading should match", Double.valueOf(100.0), response.getLastEnergyReading());
    }

    /**
     * Tests builder pattern with nullable fields.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Object can be created with null values for optional fields</li>
     *   <li>Nullable fields are handled correctly</li>
     * </ul></p>
     */
    @Test
    public void testBuilderWithNullableFields() {
        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(null)
                .lastEnergyReading(null)
                .build();

        assertNotNull("Response should not be null", response);
        assertNull("Last report time should be null", response.getLastReportTime());
        assertNull("Last energy reading should be null", response.getLastEnergyReading());
    }

    /**
     * Tests no-args constructor behavior.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Object can be created without arguments</li>
     *   <li>All fields are initially null</li>
     * </ul></p>
     */
    @Test
    public void testNoArgsConstructor() {
        DeviceResponse response = new DeviceResponse();
        
        assertNotNull("Response should not be null", response);
        assertNull("ID should be null", response.getId());
        assertNull("Serial number should be null", response.getSerialNumber());
        assertNull("Device type should be null", response.getDeviceType());
        assertNull("Status should be null", response.getStatus());
        assertNull("Last report time should be null", response.getLastReportTime());
        assertNull("Last energy reading should be null", response.getLastEnergyReading());
    }

    /**
     * Tests setter and getter methods.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Setter methods work correctly</li>
     *   <li>Getter methods return set values</li>
     *   <li>Fields can be modified after object creation</li>
     * </ul></p>
     */
    @Test
    public void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        DeviceResponse response = new DeviceResponse();
        
        response.setId(1L);
        response.setSerialNumber("TEST-001");
        response.setDeviceType("SMART_METER");
        response.setStatus(DeviceStatus.ACTIVE);
        response.setLastReportTime(now);
        response.setLastEnergyReading(100.0);

        assertEquals("ID should match", Long.valueOf(1L), response.getId());
        assertEquals("Serial number should match", "TEST-001", response.getSerialNumber());
        assertEquals("Device type should match", "SMART_METER", response.getDeviceType());
        assertEquals("Status should match", DeviceStatus.ACTIVE, response.getStatus());
        assertEquals("Last report time should match", now, response.getLastReportTime());
        assertEquals("Last energy reading should match", Double.valueOf(100.0), response.getLastEnergyReading());
    }

    /**
     * Tests equals and hashCode contract.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Objects with same content are considered equal</li>
     *   <li>Equal objects generate same hash code</li>
     * </ul></p>
     */
    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        
        DeviceResponse response1 = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(now)
                .lastEnergyReading(100.0)
                .build();

        DeviceResponse response2 = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(now)
                .lastEnergyReading(100.0)
                .build();

        assertEquals("Equal responses should be equal", response1, response2);
        assertEquals("Equal responses should have same hash code", 
                    response1.hashCode(), response2.hashCode());
    }

    /**
     * Tests toString method implementation.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>toString method is not null</li>
     *   <li>String representation contains all key fields</li>
     * </ul></p>
     */
    @Test
    public void testToString() {
        LocalDateTime now = LocalDateTime.now();
        DeviceResponse response = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(now)
                .lastEnergyReading(100.0)
                .build();

        String toString = response.toString();
        assertNotNull("ToString should not be null", toString);
        assertTrue("ToString should contain id", toString.contains("id=1"));
        assertTrue("ToString should contain serialNumber", toString.contains("serialNumber=TEST-001"));
        assertTrue("ToString should contain deviceType", toString.contains("deviceType=SMART_METER"));
        assertTrue("ToString should contain status", toString.contains("status=ACTIVE"));
        assertTrue("ToString should contain lastEnergyReading", toString.contains("lastEnergyReading=100.0"));
    }

    /**
     * Tests copy constructor or builder-based object copying.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Objects can be correctly copied</li>
     *   <li>Copied object is equal to original</li>
     * </ul></p>
     */
    @Test
    public void testCopyConstructor() {
        LocalDateTime now = LocalDateTime.now();
        DeviceResponse original = DeviceResponse.builder()
                .id(1L)
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastReportTime(now)
                .lastEnergyReading(100.0)
                .build();

        DeviceResponse copy = DeviceResponse.builder()
                .id(original.getId())
                .serialNumber(original.getSerialNumber())
                .deviceType(original.getDeviceType())
                .status(original.getStatus())
                .lastReportTime(original.getLastReportTime())
                .lastEnergyReading(original.getLastEnergyReading())
                .build();

        assertEquals("Copied response should equal original", original, copy);
    }
}