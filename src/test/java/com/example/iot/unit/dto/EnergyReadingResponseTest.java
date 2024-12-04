package com.example.iot.unit.dto;

import com.example.iot.dto.EnergyReadingResponse;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

/**
 * Unit test class for EnergyReadingResponse DTO.
 * 
 * This test suite covers various scenarios including:
 * - Builder pattern functionality
 * - No-args constructor
 * - Setter and getter methods
 * - Equals and hashCode contract
 * - ToString method behavior
 * - Handling of null and edge case values
 */
public class EnergyReadingResponseTest {

    /**
     * Tests the builder with all fields populated.
     * Verifies that all fields can be set and retrieved correctly.
     */
    @Test
    public void testBuilderWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        EnergyReadingResponse response = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(150.5)
                .timestamp(now)
                .build();

        assertNotNull("Response should not be null", response);
        assertEquals("ID should match", Long.valueOf(1L), response.getId());
        assertEquals("Device ID should match", Long.valueOf(100L), response.getDeviceId());
        assertEquals("Value should match", Double.valueOf(150.5), response.getValue());
        assertEquals("Timestamp should match", now, response.getTimestamp());
    }

    /**
     * Tests the no-args constructor.
     * Verifies that all fields are initially null.
     */
    @Test
    public void testNoArgsConstructor() {
        EnergyReadingResponse response = new EnergyReadingResponse();
        
        assertNotNull("Response should not be null", response);
        assertNull("ID should be null", response.getId());
        assertNull("Device ID should be null", response.getDeviceId());
        assertNull("Value should be null", response.getValue());
        assertNull("Timestamp should be null", response.getTimestamp());
    }

    /**
     * Tests setter and getter methods.
     * Verifies that values can be set and retrieved correctly after instantiation.
     */
    @Test
    public void testSettersAndGetters() {
        LocalDateTime now = LocalDateTime.now();
        EnergyReadingResponse response = new EnergyReadingResponse();
        
        response.setId(1L);
        response.setDeviceId(100L);
        response.setValue(150.5);
        response.setTimestamp(now);

        assertEquals("ID should match", Long.valueOf(1L), response.getId());
        assertEquals("Device ID should match", Long.valueOf(100L), response.getDeviceId());
        assertEquals("Value should match", Double.valueOf(150.5), response.getValue());
        assertEquals("Timestamp should match", now, response.getTimestamp());
    }

    /**
     * Tests the equals() and hashCode() methods.
     * Verifies that two responses with the same values are considered equal
     * and have the same hash code.
     */
    @Test
    public void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        
        EnergyReadingResponse response1 = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(150.5)
                .timestamp(now)
                .build();

        EnergyReadingResponse response2 = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(150.5)
                .timestamp(now)
                .build();

        assertEquals("Equal responses should be equal", response1, response2);
        assertEquals("Equal responses should have same hash code", 
                    response1.hashCode(), response2.hashCode());
    }

    /**
     * Tests the toString() method.
     * Verifies that the string representation contains all relevant field values.
     */
    @Test
    public void testToString() {
        LocalDateTime now = LocalDateTime.now();
        EnergyReadingResponse response = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(150.5)
                .timestamp(now)
                .build();

        String toString = response.toString();
        assertNotNull("ToString should not be null", toString);
        assertTrue("ToString should contain id", toString.contains("id=1"));
        assertTrue("ToString should contain deviceId", toString.contains("deviceId=100"));
        assertTrue("ToString should contain value", toString.contains("value=150.5"));
        assertTrue("ToString should contain timestamp", toString.contains("timestamp="));
    }

    /**
     * Tests handling of a null timestamp.
     * Verifies that the response can be created with a null timestamp.
     */
    @Test
    public void testWithNullTimestamp() {
        EnergyReadingResponse response = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(150.5)
                .timestamp(null)
                .build();

        assertNotNull("Response should not be null", response);
        assertNull("Timestamp should be null", response.getTimestamp());
    }

    /**
     * Tests handling of a zero value.
     * Verifies that zero can be set as a valid value.
     */
    @Test
    public void testWithZeroValue() {
        EnergyReadingResponse response = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(0.0)
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals("Value should be zero", Double.valueOf(0.0), response.getValue());
    }

    /**
     * Tests handling of a negative value.
     * Verifies that negative values can be set in the response object.
     */
    @Test
    public void testWithNegativeValue() {
        // Even though requests validate for positive values,
        // response objects should handle any value
        EnergyReadingResponse response = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(-150.5)
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals("Value should be negative", Double.valueOf(-150.5), response.getValue());
    }

    /**
     * Tests the copy constructor equivalent.
     * Verifies that a new response can be created with the same values as another.
     */
    @Test
    public void testCopyConstructor() {
        LocalDateTime now = LocalDateTime.now();
        EnergyReadingResponse original = EnergyReadingResponse.builder()
                .id(1L)
                .deviceId(100L)
                .value(150.5)
                .timestamp(now)
                .build();

        EnergyReadingResponse copy = EnergyReadingResponse.builder()
                .id(original.getId())
                .deviceId(original.getDeviceId())
                .value(original.getValue())
                .timestamp(original.getTimestamp())
                .build();

        assertEquals("Copied response should equal original", original, copy);
    }
}