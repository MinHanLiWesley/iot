-- Create database
CREATE DATABASE IF NOT EXISTS iot_monitoring;
USE iot_monitoring;

-- Create tables
CREATE TABLE IF NOT EXISTS devices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    serial_number VARCHAR(255) UNIQUE NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    last_report_time DATETIME,
    last_energy_reading DOUBLE
);

CREATE TABLE IF NOT EXISTS energy_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id BIGINT NOT NULL,
    energy_consumed DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (device_id) REFERENCES devices(id)
);