package com.example.iot.unit.dto;

import com.example.iot.dto.DeviceRegistrationRequest;

import static org.junit.Assert.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test suite for the DeviceRegistrationRequest DTO.
 * 
 * <p>Comprehensive validation testing for device registration request data transfer object.
 * Focuses on:
 * <ul>
 *   <li>Validation constraints</li>
 *   <li>Builder pattern functionality</li>
 *   <li>Getter and setter methods</li>
 *   <li>Equals and hashCode contract</li>
 * </ul></p>
 * 
 * <p>Test Categories:
 * <ul>
 *   <li>Valid request validation</li>
 *   <li>Null field validation</li>
 *   <li>Blank field validation</li>
 *   <li>Object creation methods</li>
 *   <li>Object comparison</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see DeviceRegistrationRequest
 */
public class DeviceRegistrationRequestTest {

    /**
     * Validator for bean validation constraints.
     * Used to validate DeviceRegistrationRequest objects.
     */
    private Validator validator;

    /**
     * Sets up the validation context before each test.
     * Initializes the validator factory and creates a validator instance.
     */
    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests a valid device registration request.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>A completely valid request passes validation</li>
     *   <li>No constraint violations are generated</li>
     * </ul></p>
     */
    @Test
    public void testValidRequest() {
        DeviceRegistrationRequest request = DeviceRegistrationRequest.builder()
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .build();

        Set<ConstraintViolation<DeviceRegistrationRequest>> violations = validator.validate(request);
        assertTrue("Valid request should have no violations", violations.isEmpty());
    }

    /**
     * Tests device registration request with null serial number.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Null serial number triggers validation error</li>
     *   <li>Correct error message is generated</li>
     * </ul></p>
     */
    @Test
    public void testNullSerialNumber() {
        DeviceRegistrationRequest request = DeviceRegistrationRequest.builder()
                .serialNumber(null)
                .deviceType("SMART_METER")
                .build();

        Set<ConstraintViolation<DeviceRegistrationRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Serial number is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests device registration request with blank serial number.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Blank serial number is rejected</li>
     *   <li>Appropriate validation error is raised</li>
     * </ul></p>
     */
    @Test
    public void testBlankSerialNumber() {
        DeviceRegistrationRequest request = DeviceRegistrationRequest.builder()
                .serialNumber("   ")
                .deviceType("SMART_METER")
                .build();

        Set<ConstraintViolation<DeviceRegistrationRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Serial number is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests device registration request with null device type.
     * 
     * <p>Checks that:
     * <ul>
     *   <li>Null device type is not allowed</li>
     *   <li>Correct validation error message is produced</li>
     * </ul></p>
     */
    @Test
    public void testNullDeviceType() {
        DeviceRegistrationRequest request = DeviceRegistrationRequest.builder()
                .serialNumber("TEST-001")
                .deviceType(null)
                .build();

        Set<ConstraintViolation<DeviceRegistrationRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Device type is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests device registration request with blank device type.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Blank device type is rejected</li>
     *   <li>Validation error is generated</li>
     * </ul></p>
     */
    @Test
    public void testBlankDeviceType() {
        DeviceRegistrationRequest request = DeviceRegistrationRequest.builder()
                .serialNumber("TEST-001")
                .deviceType("  ")
                .build();

        Set<ConstraintViolation<DeviceRegistrationRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Device type is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests device registration request with all fields null.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Multiple validation errors are generated</li>
     *   <li>All required fields are checked</li>
     * </ul></p>
     */
    @Test
    public void testAllFieldsNull() {
        DeviceRegistrationRequest request = DeviceRegistrationRequest.builder()
                .serialNumber(null)
                .deviceType(null)
                .build();

        Set<ConstraintViolation<DeviceRegistrationRequest>> violations = validator.validate(request);
        assertEquals("Should have 2 violations", 2, violations.size());
    }

    /**
     * Tests the builder pattern and getter methods.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Builder creates object correctly</li>
     *   <li>Getter methods return expected values</li>
     * </ul></p>
     */
    @Test
    public void testBuilderAndGetters() {
        DeviceRegistrationRequest request = DeviceRegistrationRequest.builder()
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .build();

        assertEquals("Serial number should match", "TEST-001", request.getSerialNumber());
        assertEquals("Device type should match", "SMART_METER", request.getDeviceType());
    }

    /**
     * Tests setter methods for the DTO.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Setter methods work correctly</li>
     *   <li>Fields can be modified after object creation</li>
     * </ul></p>
     */
    @Test
    public void testSetters() {
        DeviceRegistrationRequest request = new DeviceRegistrationRequest();
        request.setSerialNumber("TEST-001");
        request.setDeviceType("SMART_METER");

        assertEquals("Serial number should match", "TEST-001", request.getSerialNumber());
        assertEquals("Device type should match", "SMART_METER", request.getDeviceType());
    }

    /**
     * Tests equals and hashCode contract for the DTO.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Objects with same content are considered equal</li>
     *   <li>Equal objects generate same hash code</li>
     * </ul></p>
     */
    @Test
    public void testEqualsAndHashCode() {
        DeviceRegistrationRequest request1 = DeviceRegistrationRequest.builder()
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .build();

        DeviceRegistrationRequest request2 = DeviceRegistrationRequest.builder()
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .build();

        assertEquals("Equal objects should be equal", request1, request2);
        assertEquals("Equal objects should have same hash code", 
                    request1.hashCode(), request2.hashCode());
    }
}