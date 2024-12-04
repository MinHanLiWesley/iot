// Load devices when page loads
document.addEventListener('DOMContentLoaded', loadDevices);

async function loadDevices() {
    try {
        const devices = await api.getAllDevices();
        const devicesList = document.getElementById('devicesList');
        devicesList.innerHTML = devices.map(device => `
            <div class="device-card">
                <h3>Device ${device.serialNumber}</h3>
                <p>Type: ${device.deviceType}</p>
                <div class="status-container">
                    <label>Status: </label>
                    <select class="status-select" data-device-id="${device.id}" onchange="updateDeviceStatus(this)">
                        <option value="ACTIVE" ${device.status === 'ACTIVE' ? 'selected' : ''}>Active</option>
                        <option value="INACTIVE" ${device.status === 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                        <option value="MAINTENANCE" ${device.status === 'MAINTENANCE' ? 'selected' : ''}>Maintenance</option>
                    </select>
                </div>
                <p>Last Reading: ${device.lastEnergyReading || 'N/A'}</p>
                <button data-device-id="${device.id}" class="record-reading-btn">Record Reading</button>
                <button onclick="showDeviceDetails(${device.id})">View Details</button>
            </div>
        `).join('');

        // Add event listeners to all record reading buttons
        document.querySelectorAll('.record-reading-btn').forEach(button => {
            button.addEventListener('click', function() {
                const deviceId = this.getAttribute('data-device-id');
                recordReading(deviceId);
            });
        });
    } catch (error) {
        console.error('Error loading devices:', error);
        alert('Error loading devices. Check console for details.');
    }
}

async function recordReading(deviceId) {
    const value = prompt("Enter energy consumption value:");
    if (value && !isNaN(value)) {
        try {
            const response = await api.recordReading(deviceId, parseFloat(value));
            console.log('Reading recorded:', response);
            alert('Reading recorded successfully!');
            loadDevices();
        } catch (error) {
            console.error('Error recording reading:', error);
            alert('Failed to record reading. Check console for details.');
        }
    } else if (value !== null) {
        alert('Please enter a valid number.');
    }
}

function showAddDeviceForm() {
    document.getElementById('addDeviceModal').style.display = 'block';
}

// Close modal when clicking outside
window.onclick = function(event) {
    const addDeviceModal = document.getElementById('addDeviceModal');
    const deviceDetailsModal = document.getElementById('deviceDetailsModal');
    
    if (event.target === addDeviceModal) {
        addDeviceModal.style.display = 'none';
    }
    if (event.target === deviceDetailsModal) {
        deviceDetailsModal.style.display = 'none';
    }
}

document.getElementById('addDeviceForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        serialNumber: document.getElementById('serialNumber').value,
        deviceType: document.getElementById('deviceType').value
    };
    
    try {
        await api.registerDevice(data);
        document.getElementById('addDeviceModal').style.display = 'none';
        document.getElementById('addDeviceForm').reset();
        alert('Device added successfully!');
        loadDevices();
    } catch (error) {
        console.error('Error adding device:', error);
        alert('Error adding device. Check console for details.');
    }
});

async function fetchReadings() {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    
    if (!startDate || !endDate) {
        alert('Please select both start and end dates');
        return;
    }

    try {
        const devices = await api.getAllDevices();
        let allReadings = [];
        
        for (const device of devices) {
            const readings = await api.getReadings(
                device.id, 
                new Date(startDate).toISOString(), 
                new Date(endDate).toISOString()
            );
            allReadings = allReadings.concat(readings);
        }

        displayReadings(allReadings);
    } catch (error) {
        console.error('Error fetching readings:', error);
        alert('Error fetching readings. Check console for details.');
    }
}

function displayReadings(readings) {
    const readingsList = document.getElementById('readingsList');
    
    if (readings.length === 0) {
        readingsList.innerHTML = '<p>No readings found for the selected period.</p>';
        return;
    }

    const table = `
        <table class="readings-table">
            <thead>
                <tr>
                    <th>Device</th>
                    <th>Value</th>
                    <th>Timestamp</th>
                </tr>
            </thead>
            <tbody>
                ${readings.map(reading => `
                    <tr>
                        <td>${reading.deviceSerialNumber}</td>
                        <td>${reading.value}</td>
                        <td>${new Date(reading.timestamp).toLocaleString()}</td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
    
    readingsList.innerHTML = table;
}

// Set default date range (last 24 hours)
document.addEventListener('DOMContentLoaded', () => {
    const now = new Date();
    const yesterday = new Date(now);
    yesterday.setDate(yesterday.getDate() - 1);
    
    document.getElementById('startDate').value = yesterday.toISOString().slice(0, 16);
    document.getElementById('endDate').value = now.toISOString().slice(0, 16);
    
    loadDevices();
});

async function showDeviceDetails(deviceId) {
    try {
        const device = await api.getDevice(deviceId);
        const latestReading = await api.getLatestReading(deviceId);
        const averageConsumption = await api.getAverageConsumption(deviceId);

        const detailsHtml = `
            <div class="device-details">
                <p><strong>Serial Number:</strong> ${device.serialNumber}</p>
                <p><strong>Type:</strong> ${device.deviceType}</p>
                <p><strong>Status:</strong> ${device.status}</p>
                <p><strong>Latest Reading:</strong> ${latestReading?.value || 'N/A'}</p>
                <p><strong>Average Consumption:</strong> ${averageConsumption || 'N/A'}</p>
                <p><strong>Last Report Time:</strong> ${device.lastReportTime ? new Date(device.lastReportTime).toLocaleString() : 'N/A'}</p>
            </div>
        `;

        document.getElementById('deviceDetailsContent').innerHTML = detailsHtml;
        document.getElementById('deviceDetailsModal').style.display = 'block';
    } catch (error) {
        console.error('Error fetching device details:', error);
        alert('Error fetching device details');
    }
}
async function updateDeviceStatus(selectElement) {
    const deviceId = selectElement.getAttribute('data-device-id');
    const newStatus = selectElement.value;
    
    try {
        await api.updateDeviceStatus(deviceId, newStatus);
        alert(`Device status updated to ${newStatus}`);
        loadDevices(); // Refresh the device list
    } catch (error) {
        console.error('Error updating device status:', error);
        alert('Failed to update device status. Check console for details.');
        loadDevices(); // Refresh to revert the select to the previous state
    }
}
