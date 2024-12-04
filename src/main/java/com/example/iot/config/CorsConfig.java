package com.example.iot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) settings.
 * Defines CORS policies for the IoT Energy Monitoring API endpoints.
 *
 * <p>This configuration:
 * <ul>
 *   <li>Applies to all API endpoints under /api/</li>
 *   <li>Allows access from the frontend development server</li>
 *   <li>Permits standard HTTP methods for REST operations</li>
 *   <li>Enables necessary request headers</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * Configures CORS mappings for the application.
     * Allows controlled access from frontend applications.
     *
     * @param registry CorsRegistry to configure CORS policies
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8081")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*");
    }
}