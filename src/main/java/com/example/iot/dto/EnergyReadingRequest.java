package com.example.iot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for submitting new energy consumption readings.
 * Contains the energy consumption value and optional metadata.
 *
 * <p>Required fields:
 * <ul>
 *   <li>value - The energy consumption reading</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyReadingRequest {
    /**
     * Energy consumption value.
     * Must be a positive number representing the reading.
     */
    @NotNull(message = "Energy reading value is required")
    @Positive(message = "Energy reading must be positive")
    private Double value;
}
