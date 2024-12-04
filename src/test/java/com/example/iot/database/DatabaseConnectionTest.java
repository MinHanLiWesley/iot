package com.example.iot.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() {
        try {
            // Test MYSQL Version
            String version = jdbcTemplate.queryForObject(
                    "SELECT VERSION()", String.class);
            System.out.println("Connected to MYSQL version: " + version);

            // Test database connection and settings
            String dbName = jdbcTemplate.queryForObject(
                    "SELECT DATABASE()", String.class);
            System.out.println("Connected to database: " + dbName);

            // Get database characteristics
            jdbcTemplate.queryForList(
                    "SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME " +
                            "FROM information_schema.SCHEMATA " +
                            "WHERE SCHEMA_NAME = ?", dbName)
                    .forEach(row -> {
                        System.out.println("Character Set: " + row.get("DEFAULT_CHARACTER_SET_NAME"));
                        System.out.println("Collation: " + row.get("DEFAULT_COLLATION_NAME"));
                    });

            // Test table creation capability
            jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS connection_test " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, test_column VARCHAR(255))");
            System.out.println("Table creation test successful");

            // Cleanup test table
            jdbcTemplate.execute("DROP TABLE IF EXISTS connection_test");
        } catch (Exception e) {
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nTroubleshooting for MySQL 8.4.3:");
            System.err.println("1. Verify MySQL service is running");
            System.err.println("2. Check credentials in application.properties");
            System.err.println("3. Confirm database exists and is accessible");
            System.err.println("4. Verify user privileges");
            throw e; // Rethrow to fail the test
        }
    }
}