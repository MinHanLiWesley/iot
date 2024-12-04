package com.example.iot.repository;

import com.example.iot.model.Device;
import com.example.iot.model.DeviceStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Device entity operations.
 * Provides data access methods for managing IoT devices in the database.
 *
 * <p>This repository extends JpaRepository to inherit basic CRUD operations and adds:
 * <ul>
 *   <li>Serial number based queries</li>
 *   <li>Status based filtering</li>
 *   <li>Energy consumption threshold queries</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.model.Device
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
  /**
   * Finds a device by its unique serial number.
   *
   * @param serialNumber The serial number to search for
   * @return Optional containing the device if found
   */
  Optional<Device> findBySerialNumber(String serialNumber);

  /**
   * Checks if a device with the given serial number exists.
   *
   * @param serialNumber The serial number to check
   * @return true if a device exists with this serial number
   */
  boolean existsBySerialNumber(String serialNumber);

  /**
   * Retrieves all devices with a specific operational status.
   *
   * @param status The status to filter by
   * @return List of devices matching the status
   */
  List<Device> findByStatus(DeviceStatus status);

  /**
   * Finds devices with energy consumption above a specified threshold.
   *
   * @param threshold The minimum energy reading value
   * @return List of devices with readings above the threshold
   */
  @Query("SELECT d FROM Device d WHERE d.lastEnergyReading > :threshold")
  List<Device> findDevicesWithHighEnergyConsumption(Double threshold);
}
