package com.example.iot.unit.repository;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.iot.model.Device;
import com.example.iot.model.DeviceStatus;
import com.example.iot.model.EnergyData;
import com.example.iot.repository.EnergyDataRepository;

/**
 * Unit test suite for the EnergyDataRepository.
 * 
 * <p>Comprehensive testing for energy data repository data access methods, covering:
 * <ul>
 *   <li>Energy reading retrieval by device and timestamp</li>
 *   <li>Average energy consumption calculation</li>
 *   <li>Latest energy reading retrieval</li>
 *   <li>Edge case handling for no readings</li>
 * </ul></p>
 * 
 * <p>Testing Strategies:
 * <ul>
 *   <li>Uses Spring Boot's DataJpaTest for repository testing</li>
 *   <li>Leverages TestEntityManager for test data persistence</li>
 *   <li>Validates various query methods</li>
 *   <li>Checks both positive and negative scenarios</li>
 * </ul></p>
 * 
 * @author Min-Han Li
 * @version 1.0
 * @see EnergyDataRepository
 * @see EnergyData
 * @see Device
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class EnergyDataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnergyDataRepository energyDataRepository;

    /**
     * Helper method to create and persist a test device.
     * 
     * <p>Simplifies device creation for multiple test scenarios.</p>
     * 
     * @param serialNumber Serial number for the test device
     * @return Persisted Device entity
     */
    private Device createAndPersistDevice(String serialNumber) {
        Device device = Device.builder()
                .serialNumber(serialNumber)
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();
        return entityManager.persist(device);
    }

    /**
     * Tests retrieving energy readings within a specific timestamp range.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Readings can be retrieved for a specific device and time range</li>
     *   <li>Correct number of readings are returned</li>
     *   <li>Only readings within the specified range are included</li>
     * </ul></p>
     */
    @Test
    public void testFindByDeviceAndTimestampBetween() {
        Device device = createAndPersistDevice("TEST-001");
        LocalDateTime now = LocalDateTime.now();

        // Create readings at different times
        EnergyData reading1 = EnergyData.builder()
                .device(device)
                .energyConsumed(100.0)
                .timestamp(now.minusHours(3))
                .build();

        EnergyData reading2 = EnergyData.builder()
                .device(device)
                .energyConsumed(150.0)
                .timestamp(now.minusHours(2))
                .build();

        EnergyData reading3 = EnergyData.builder()
                .device(device)
                .energyConsumed(200.0)
                .timestamp(now.minusHours(1))
                .build();

        entityManager.persist(reading1);
        entityManager.persist(reading2);
        entityManager.persist(reading3);
        entityManager.flush();

        // Test finding readings within a time range
        List<EnergyData> readings = energyDataRepository.findByDeviceAndTimestampBetween(
                device, 
                now.minusHours(2).minusMinutes(30), 
                now.minusMinutes(30)
        );

        assertEquals("Should find 2 readings", 2, readings.size());
        assertTrue("Should include middle reading", 
                readings.stream().anyMatch(r -> r.getEnergyConsumed().equals(150.0)));
        assertTrue("Should include latest reading", 
                readings.stream().anyMatch(r -> r.getEnergyConsumed().equals(200.0)));
    }

    /**
     * Tests calculation of average energy consumption for a device.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Average is correctly calculated from multiple readings</li>
     *   <li>Calculation handles multiple energy readings</li>
     * </ul></p>
     */
    @Test
    public void testCalculateAverageConsumption() {
        Device device = createAndPersistDevice("TEST-002");
        LocalDateTime now = LocalDateTime.now();

        // Create readings with different values
        EnergyData reading1 = EnergyData.builder()
                .device(device)
                .energyConsumed(100.0)
                .timestamp(now.minusHours(2))
                .build();

        EnergyData reading2 = EnergyData.builder()
                .device(device)
                .energyConsumed(200.0)
                .timestamp(now.minusHours(1))
                .build();

        entityManager.persist(reading1);
        entityManager.persist(reading2);
        entityManager.flush();

        Double average = energyDataRepository.calculateAverageConsumption(device);
        assertEquals("Average should be 150.0", 150.0, average, 0.01);
    }

    /**
     * Tests average consumption calculation when no readings exist.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>No average is returned for a device without readings</li>
     *   <li>Null is returned in absence of readings</li>
     * </ul></p>
     */
    @Test
    public void testCalculateAverageConsumptionNoReadings() {
        Device device = createAndPersistDevice("TEST-003");
        Double average = energyDataRepository.calculateAverageConsumption(device);
        assertNull("Average should be null when no readings exist", average);
    }

    /**
     * Tests retrieval of the latest energy reading for a device.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Most recent reading is correctly identified</li>
     *   <li>Reading with latest timestamp is returned</li>
     * </ul></p>
     */
    @Test
    public void testFindLatestReading() {
        Device device = createAndPersistDevice("TEST-004");
        LocalDateTime now = LocalDateTime.now();

        EnergyData reading1 = EnergyData.builder()
                .device(device)
                .energyConsumed(100.0)
                .timestamp(now.minusHours(2))
                .build();

        EnergyData reading2 = EnergyData.builder()
                .device(device)
                .energyConsumed(200.0)
                .timestamp(now.minusHours(1))
                .build();

        entityManager.persist(reading1);
        entityManager.persist(reading2);
        entityManager.flush();

        Optional<EnergyData> latest = energyDataRepository.findLatestReading(device);
        assertTrue("Should find latest reading", latest.isPresent());
        assertEquals("Should return most recent reading", 200.0, latest.get().getEnergyConsumed(), 0.01);
        assertEquals("Should have correct timestamp", now.minusHours(1), latest.get().getTimestamp());
    }

    /**
     * Tests latest reading retrieval when no readings exist.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>No reading is returned for a device without readings</li>
     *   <li>Optional is empty when no readings exist</li>
     * </ul></p>
     */
    @Test
    public void testFindLatestReadingNoReadings() {
        Device device = createAndPersistDevice("TEST-005");
        Optional<EnergyData> latest = energyDataRepository.findLatestReading(device);
        assertFalse("Should not find any reading", latest.isPresent());
    }

    /**
     * Tests device reading retrieval with no matching readings.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Empty list is returned when no readings match criteria</li>
     *   <li>No exceptions are thrown for non-existent readings</li>
     * </ul></p>
     */
    @Test
    public void testFindByDeviceAndTimestampBetweenNoReadings() {
        Device device = createAndPersistDevice("TEST-006");
        LocalDateTime now = LocalDateTime.now();

        List<EnergyData> readings = energyDataRepository.findByDeviceAndTimestampBetween(
                device,
                now.minusHours(2),
                now
        );

        assertTrue("Should return empty list when no readings exist", readings.isEmpty());
    }
}
