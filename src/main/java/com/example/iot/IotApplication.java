package com.example.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the IoT Energy Monitoring System.
 * This Spring Boot application provides functionality for monitoring and managing IoT devices
 * that collect energy consumption data.
 * 
 * <p>The application uses Spring Boot's auto-configuration capabilities and scans for components
 * in the "com.example.iot" package and its sub-packages.</p>
 * 
 * <p>Key features include:
 * <ul>
 *   <li>Device management (registration, updates, deletion)</li>
 *   <li>Energy consumption monitoring</li>
 *   <li>Data analysis capabilities</li>
 *   <li>RESTful API endpoints</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @since 2024-12-03
 */
@SpringBootApplication(scanBasePackages = "com.example.iot")
public class IotApplication {

    /**
     * The main entry point of the application.
     * Starts the Spring Boot application with the specified arguments.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(IotApplication.class, args);
    }

}
