package com.example.iot.unit.dto;

import com.example.iot.dto.DeviceUpdateRequest;

import static org.junit.Assert.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the DeviceUpdateRequest DTO class.
 * These tests verify the validation, building, and basic object operations
 * of the DeviceUpdateRequest class.
 */
public class DeviceUpdateRequestTest {

    private Validator validator;

    /**
     * Sets up the validator before each test.
     */
    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests that a valid request with all fields populated passes validation.
     */
    @Test
    public void testValidRequest() {
        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .deviceType("SMART_METER")
                .serialNumber("TEST-001")
                .build();

        Set<ConstraintViolation<DeviceUpdateRequest>> violations = validator.validate(request);
        assertTrue("Valid request should have no violations", violations.isEmpty());
    }

    /**
     * Tests that a request without a serial number is still valid,
     * as the serial number is optional in update requests.
     */
    @Test
    public void testValidRequestWithoutSerialNumber() {
        // serialNumber is optional in update request
        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .deviceType("SMART_METER")
                .build();

        Set<ConstraintViolation<DeviceUpdateRequest>> violations = validator.validate(request);
        assertTrue("Valid request without serial number should have no violations", violations.isEmpty());
    }

    /**
     * Tests that validation fails when device type is null.
     * Expects a single violation with the message "Device type is required".
     */
    @Test
    public void testNullDeviceType() {
        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .deviceType(null)
                .serialNumber("TEST-001")
                .build();

        Set<ConstraintViolation<DeviceUpdateRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Device type is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests that validation fails when device type contains only whitespace.
     * Expects a single violation with the message "Device type is required".
     */
    @Test
    public void testBlankDeviceType() {
        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .deviceType("   ")
                .serialNumber("TEST-001")
                .build();

        Set<ConstraintViolation<DeviceUpdateRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Device type is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests the builder pattern and getter methods of DeviceUpdateRequest.
     * Verifies that values set through the builder can be retrieved correctly.
     */
    @Test
    public void testBuilderAndGetters() {
        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .deviceType("SMART_METER")
                .serialNumber("TEST-001")
                .build();

        assertEquals("Device type should match", "SMART_METER", request.getDeviceType());
        assertEquals("Serial number should match", "TEST-001", request.getSerialNumber());
    }

    /**
     * Tests the setter methods of DeviceUpdateRequest.
     * Verifies that values set through setters can be retrieved correctly.
     */
    @Test
    public void testSetters() {
        DeviceUpdateRequest request = new DeviceUpdateRequest();
        request.setDeviceType("SMART_METER");
        request.setSerialNumber("TEST-001");

        assertEquals("Device type should match", "SMART_METER", request.getDeviceType());
        assertEquals("Serial number should match", "TEST-001", request.getSerialNumber());
    }

    /**
     * Tests equals() and hashCode() methods.
     * Verifies that two objects with the same values are considered equal
     * and have the same hash code.
     */
    @Test
    public void testEqualsAndHashCode() {
        DeviceUpdateRequest request1 = DeviceUpdateRequest.builder()
                .deviceType("SMART_METER")
                .serialNumber("TEST-001")
                .build();

        DeviceUpdateRequest request2 = DeviceUpdateRequest.builder()
                .deviceType("SMART_METER")
                .serialNumber("TEST-001")
                .build();

        assertEquals("Equal objects should be equal", request1, request2);
        assertEquals("Equal objects should have same hash code", 
                    request1.hashCode(), request2.hashCode());
    }

    /**
     * Tests the toString() method.
     * Verifies that the string representation contains all relevant field values.
     */
    @Test
    public void testToString() {
        DeviceUpdateRequest request = DeviceUpdateRequest.builder()
                .deviceType("SMART_METER")
                .serialNumber("TEST-001")
                .build();

        String toString = request.toString();
        assertNotNull("ToString should not be null", toString);
        assertTrue("ToString should contain deviceType", toString.contains("deviceType=SMART_METER"));
        assertTrue("ToString should contain serialNumber", toString.contains("serialNumber=TEST-001"));
    }
}