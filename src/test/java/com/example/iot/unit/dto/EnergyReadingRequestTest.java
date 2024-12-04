package com.example.iot.unit.dto;

import com.example.iot.dto.EnergyReadingRequest;

import static org.junit.Assert.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test class for EnergyReadingRequest DTO.
 * 
 * This test suite covers various scenarios including:
 * - Validation of energy reading values
 * - Builder pattern functionality
 * - Setter and getter methods
 * - Equals and hashCode contract
 * - ToString method behavior
 */
public class EnergyReadingRequestTest {

    private Validator validator;

    /**
     * Sets up the validator factory before each test method.
     * Initializes a validator for bean validation.
     */
    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Tests that a valid energy reading request passes validation.
     * Verifies that a request with a positive value has no constraint violations.
     */
    @Test
    public void testValidRequest() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        Set<ConstraintViolation<EnergyReadingRequest>> violations = validator.validate(request);
        assertTrue("Valid request should have no violations", violations.isEmpty());
    }

    /**
     * Tests validation failure when the energy reading value is null.
     * Expects a single constraint violation with a specific error message.
     */
    @Test
    public void testNullValue() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(null)
                .build();

        Set<ConstraintViolation<EnergyReadingRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Energy reading value is required",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests validation failure when the energy reading value is negative.
     * Expects a single constraint violation with a specific error message.
     */
    @Test
    public void testNegativeValue() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(-100.0)
                .build();

        Set<ConstraintViolation<EnergyReadingRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Energy reading must be positive",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests validation failure when the energy reading value is zero.
     * Expects a single constraint violation with a specific error message.
     */
    @Test
    public void testZeroValue() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(0.0)
                .build();

        Set<ConstraintViolation<EnergyReadingRequest>> violations = validator.validate(request);
        assertEquals("Should have 1 violation", 1, violations.size());
        assertEquals("Should have correct error message", 
                    "Energy reading must be positive",
                    violations.iterator().next().getMessage());
    }

    /**
     * Tests the builder pattern and getter methods.
     * Verifies that values set through the builder can be correctly retrieved.
     */
    @Test
    public void testBuilderAndGetters() {
        Double testValue = 150.5;
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(testValue)
                .build();

        assertEquals("Value should match", testValue, request.getValue());
    }

    /**
     * Tests the setter methods of the EnergyReadingRequest.
     * Verifies that values can be set and retrieved correctly.
     */
    @Test
    public void testSetters() {
        Double testValue = 200.75;
        EnergyReadingRequest request = new EnergyReadingRequest();
        request.setValue(testValue);

        assertEquals("Value should match", testValue, request.getValue());
    }

    /**
     * Tests the equals() and hashCode() methods.
     * Verifies that two requests with the same value are considered equal
     * and have the same hash code.
     */
    @Test
    public void testEqualsAndHashCode() {
        EnergyReadingRequest request1 = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        EnergyReadingRequest request2 = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        assertEquals("Equal objects should be equal", request1, request2);
        assertEquals("Equal objects should have same hash code", 
                    request1.hashCode(), request2.hashCode());
    }

    /**
     * Tests the toString() method.
     * Verifies that the string representation contains the correct value.
     */
    @Test
    public void testToString() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(100.0)
                .build();

        String toString = request.toString();
        assertNotNull("ToString should not be null", toString);
        assertTrue("ToString should contain value", toString.contains("value=100.0"));
    }

    /**
     * Tests validation with a very large positive value.
     * Verifies that extremely large but positive values are considered valid.
     */
    @Test
    public void testVeryLargeValue() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(Double.MAX_VALUE)
                .build();

        Set<ConstraintViolation<EnergyReadingRequest>> violations = validator.validate(request);
        assertTrue("Very large positive value should be valid", violations.isEmpty());
    }

    /**
     * Tests validation with a small positive value.
     * Verifies that very small but positive values are considered valid.
     */
    @Test
    public void testSmallPositiveValue() {
        EnergyReadingRequest request = EnergyReadingRequest.builder()
                .value(0.000001)
                .build();

        Set<ConstraintViolation<EnergyReadingRequest>> violations = validator.validate(request);
        assertTrue("Small positive value should be valid", violations.isEmpty());
    }
}