package com.example.iot.unit.dto;

import static org.junit.Assert.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

import com.example.iot.dto.DeviceStatusUpdateRequest;
import com.example.iot.model.DeviceStatus;

/**
 * Unit test suite for the DeviceStatusUpdateRequest DTO.
 * 
 * <p>Comprehensive testing for device status update request data transfer object.
 * Focuses on validating:
 * <ul>
 *   <li>Validation constraints</li>
 *   <li>Getter and setter methods</li>
 *   <li>Equals and hashCode contract</li>
 *   <li>toString method</li>
 * </ul></p>
 * 
 * <p>Test Categories:
 * <ul>
 *   <li>Valid request validation</li>
 *   <li>Null status handling</li>
 *   <li>Object creation methods</li>
 *   <li>Object comparison</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see DeviceStatusUpdateRequest
 * @see DeviceStatus
 */
public class DeviceStatusUpdateRequestTest {

    /**
     * Validator for bean validation constraints.
     * Used to validate DeviceStatusUpdateRequest objects.
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
     * Tests a valid device status update request.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>A request with a valid status passes validation</li>
     *   <li>No constraint violations are generated</li>
     * </ul></p>
     */
    @Test
    public void testValidRequest() {
        DeviceStatusUpdateRequest request = new DeviceStatusUpdateRequest();
        request.setStatus(DeviceStatus.ACTIVE);

        Set<ConstraintViolation<DeviceStatusUpdateRequest>> violations = validator.validate(request);
        assertTrue("Valid request should have no violations", violations.isEmpty());
    }

    /**
     * Tests device status update request with null status.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Null status triggers validation error</li>
     *   <li>Correct error message is generated</li>
     * </ul></p>
     */
    @Test
    public void testNullStatus() {
        DeviceStatusUpdateRequest request = new DeviceStatusUpdateRequest();
        request.setStatus(null);

        Set<ConstraintViolation<DeviceStatusUpdateRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Status is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests the builder pattern and getter methods.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Setter method works correctly</li>
     *   <li>Getter method returns set value</li>
     * </ul></p>
     */
    @Test
    public void testBuilderAndGetters() {
        DeviceStatusUpdateRequest request = new DeviceStatusUpdateRequest();
        request.setStatus(DeviceStatus.INACTIVE);

        assertEquals("Status should match", DeviceStatus.INACTIVE, request.getStatus());
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
        DeviceStatusUpdateRequest request1 = new DeviceStatusUpdateRequest();
        request1.setStatus(DeviceStatus.ACTIVE);

        DeviceStatusUpdateRequest request2 = new DeviceStatusUpdateRequest();
        request2.setStatus(DeviceStatus.ACTIVE);

        assertEquals("Equal objects should be equal", request1, request2);
        assertEquals("Equal objects should have same hash code", 
                    request1.hashCode(), request2.hashCode());
    }

    /**
     * Tests toString method implementation.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>toString method is not null</li>
     *   <li>String representation contains status</li>
     * </ul></p>
     */
    @Test
    public void testToString() {
        DeviceStatusUpdateRequest request = new DeviceStatusUpdateRequest();
        request.setStatus(DeviceStatus.ACTIVE);

        String toString = request.toString();
        assertNotNull("ToString should not be null", toString);
        assertTrue("ToString should contain status", toString.contains("status=ACTIVE"));
    }
}