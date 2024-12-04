package com.example.iot.unit.repository;

import static org.junit.Assert.*;

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
import com.example.iot.repository.DeviceRepository;

/**
 * Unit test suite for the DeviceRepository.
 * 
 * <p>Comprehensive testing for device repository data access methods, covering:
 * <ul>
 *   <li>Device retrieval by serial number</li>
 *   <li>Serial number existence checks</li>
 *   <li>Device status-based filtering</li>
 *   <li>Energy consumption-based device querying</li>
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
 * @see DeviceRepository
 * @see Device
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DeviceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * Tests finding a device by its serial number.
     * 
     * <p>Verifies that:
     * <ul>
     *   <li>Device can be retrieved using existing serial number</li>
     *   <li>Retrieved device matches the persisted device</li>
     * </ul></p>
     */
    @Test
    public void testFindBySerialNumber() {
        Device device = Device.builder()
                .serialNumber("TEST-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();
        entityManager.persist(device);
        entityManager.flush();

        Optional<Device> found = deviceRepository.findBySerialNumber("TEST-001");
        assertTrue("Device should be found", found.isPresent());
        assertEquals("Serial number should match", "TEST-001", found.get().getSerialNumber());
    }

    /**
     * Tests finding a device by non-existent serial number.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>No device is found for non-existent serial number</li>
     *   <li>Optional is empty</li>
     * </ul></p>
     */
    @Test
    public void testFindBySerialNumberNotFound() {
        Optional<Device> found = deviceRepository.findBySerialNumber("NON-EXISTENT");
        assertFalse("Device should not be found", found.isPresent());
    }

    /**
     * Tests checking existence of a device by serial number.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Existing serial number returns true</li>
     *   <li>Non-existing serial number returns false</li>
     * </ul></p>
     */
    @Test
    public void testExistsBySerialNumber() {
        Device device = Device.builder()
                .serialNumber("TEST-002")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();
        entityManager.persist(device);
        entityManager.flush();

        assertTrue("Should return true for existing serial number", 
                  deviceRepository.existsBySerialNumber("TEST-002"));
        assertFalse("Should return false for non-existing serial number", 
                   deviceRepository.existsBySerialNumber("NON-EXISTENT"));
    }

    /**
     * Tests finding devices by their status.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>Devices can be retrieved by different statuses</li>
     *   <li>Correct number of devices are returned for each status</li>
     *   <li>Retrieved devices have matching status</li>
     * </ul></p>
     */
    @Test
    public void testFindByStatus() {
        // Create devices with different statuses
        Device activeDevice = Device.builder()
                .serialNumber("ACTIVE-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .build();
        
        Device inactiveDevice = Device.builder()
                .serialNumber("INACTIVE-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.INACTIVE)
                .build();
        
        Device maintenanceDevice = Device.builder()
                .serialNumber("MAINTENANCE-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.MAINTENANCE)
                .build();
        
        entityManager.persist(activeDevice);
        entityManager.persist(inactiveDevice);
        entityManager.persist(maintenanceDevice);
        entityManager.flush();

        // Test finding by each status
        List<Device> activeDevices = deviceRepository.findByStatus(DeviceStatus.ACTIVE);
        assertEquals("Should find one active device", 1, activeDevices.size());
        assertEquals("Device status should be ACTIVE", DeviceStatus.ACTIVE, activeDevices.get(0).getStatus());

        List<Device> inactiveDevices = deviceRepository.findByStatus(DeviceStatus.INACTIVE);
        assertEquals("Should find one inactive device", 1, inactiveDevices.size());
        assertEquals("Device status should be INACTIVE", DeviceStatus.INACTIVE, inactiveDevices.get(0).getStatus());

        List<Device> maintenanceDevices = deviceRepository.findByStatus(DeviceStatus.MAINTENANCE);
        assertEquals("Should find one maintenance device", 1, maintenanceDevices.size());
        assertEquals("Device status should be MAINTENANCE", DeviceStatus.MAINTENANCE, maintenanceDevices.get(0).getStatus());
    }

    /**
     * Tests finding devices with high energy consumption.
     * 
     * <p>Validates that:
     * <ul>
     *   <li>Devices above energy consumption threshold are retrieved</li>
     *   <li>Correct number of high-consumption devices are returned</li>
     *   <li>Only devices exceeding threshold are included</li>
     * </ul></p>
     */
    @Test
    public void testFindDevicesWithHighEnergyConsumption() {
        // Create devices with different energy readings
        Device highConsumptionDevice1 = Device.builder()
                .serialNumber("HIGH-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastEnergyReading(150.0)
                .build();
        
        Device highConsumptionDevice2 = Device.builder()
                .serialNumber("HIGH-002")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastEnergyReading(200.0)
                .build();
        
        Device lowConsumptionDevice = Device.builder()
                .serialNumber("LOW-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastEnergyReading(50.0)
                .build();
        
        entityManager.persist(highConsumptionDevice1);
        entityManager.persist(highConsumptionDevice2);
        entityManager.persist(lowConsumptionDevice);
        entityManager.flush();

        List<Device> highConsumptionDevices = deviceRepository.findDevicesWithHighEnergyConsumption(100.0);
        assertEquals("Should find two high consumption devices", 2, highConsumptionDevices.size());
        assertTrue("Should contain first high consumption device", 
                  highConsumptionDevices.stream().anyMatch(d -> d.getSerialNumber().equals("HIGH-001")));
        assertTrue("Should contain second high consumption device", 
                  highConsumptionDevices.stream().anyMatch(d -> d.getSerialNumber().equals("HIGH-002")));
    }

    /**
     * Tests finding devices with high energy consumption when no devices qualify.
     * 
     * <p>Ensures that:
     * <ul>
     *   <li>No devices are returned when threshold is not met</li>
     *   <li>Empty list is returned</li>
     * </ul></p>
     */
    @Test
    public void testFindDevicesWithHighEnergyConsumptionNoResults() {
        Device lowConsumptionDevice = Device.builder()
                .serialNumber("LOW-001")
                .deviceType("SMART_METER")
                .status(DeviceStatus.ACTIVE)
                .lastEnergyReading(50.0)
                .build();
        
        entityManager.persist(lowConsumptionDevice);
        entityManager.flush();

        List<Device> highConsumptionDevices = deviceRepository.findDevicesWithHighEnergyConsumption(100.0);
        assertTrue("Should find no devices", highConsumptionDevices.isEmpty());
    }
}
