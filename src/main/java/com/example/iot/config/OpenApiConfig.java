package com.example.iot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * Provides API documentation configuration for the IoT Energy Monitoring System.
 *
 * <p>This configuration:
 * <ul>
 *   <li>Sets up OpenAPI documentation metadata</li>
 *   <li>Defines API information including title and version</li>
 *   <li>Enables Swagger UI for API exploration</li>
 * </ul></p>
 *
 * <p>The API documentation can be accessed at:
 * <ul>
 *   <li>Swagger UI: /swagger-ui.html</li>
 *   <li>OpenAPI JSON: /v3/api-docs</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "IoT Energy Monitoring API",
        version = "1.0",
        description = "API Documentation for IoT Energy Monitoring System"
    )
)
public class OpenApiConfig {
}