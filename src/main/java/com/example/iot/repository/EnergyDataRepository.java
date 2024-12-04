package com.example.iot.repository;

import com.example.iot.model.Device;
import com.example.iot.model.EnergyData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EnergyData entity operations.
 * Provides data access methods for managing energy consumption readings in the database.
 *
 * <p>This repository extends JpaRepository to inherit basic CRUD operations and adds:
 * <ul>
 *   <li>Time-based data retrieval</li>
 *   <li>Statistical calculations</li>
 *   <li>Latest reading queries</li>
 * </ul></p>
 *
 * @author Min-Han Li
 * @version 1.0
 * @see com.example.iot.model.EnergyData
 * @see com.example.iot.model.Device
 */
@Repository
public interface EnergyDataRepository extends JpaRepository<EnergyData, Long> {

    /**
     * Retrieves energy readings for a specific device within a time range.
     *
     * @param device The device to get readings for
     * @param startTime Start of the time range
     * @param endTime End of the time range
     * @return List of energy readings within the specified time period
     */
    List<EnergyData> findByDeviceAndTimestampBetween(
            Device device,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    /**
     * Calculates the average energy consumption for a specific device.
     *
     * @param device The device to calculate average for
     * @return Average energy consumption value, or null if no readings exist
     */
    @Query("SELECT AVG(e.energyConsumed) FROM EnergyData e WHERE e.device = :device")
    Double calculateAverageConsumption(Device device);

    /**
     * Finds the most recent energy reading for a specific device.
     *
     * @param device The device to get the latest reading for
     * @return Optional containing the most recent reading if exists
     */
    @Query("SELECT e FROM EnergyData e WHERE e.device = :device ORDER BY e.timestamp DESC LIMIT 1")
    Optional<EnergyData> findLatestReading(Device device);
}