const API_BASE_URL = 'http://localhost:8080/api';

const api = {
    // Device endpoints
    getAllDevices: async () => {
        const response = await fetch(`${API_BASE_URL}/devices`);
        return response.json();
    },

    registerDevice: async (data) => {
        const response = await fetch(`${API_BASE_URL}/devices`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        });
        return response.json();
    },

    // Energy data endpoints
    getLatestReading: async (deviceId) => {
        const response = await fetch(`${API_BASE_URL}/devices/${deviceId}/readings/latest`);
        return response.json();
    },

    recordReading: async (deviceId, value) => {
        const response = await fetch(`${API_BASE_URL}/devices/${deviceId}/readings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ value })
        });
        return response.json();
    },

    getReadings: async (deviceId, startDate, endDate) => {
        const response = await fetch(
            `${API_BASE_URL}/devices/${deviceId}/readings?startDate=${startDate}&endDate=${endDate}`
        );
        return response.json();
    },

    getAverageConsumption: async (deviceId) => {
        const response = await fetch(`${API_BASE_URL}/devices/${deviceId}/readings/average`);
        return response.json();
    },

    getDevice: async (id) => {
        const response = await fetch(`${API_BASE_URL}/devices/${id}`);
        return response.json();
    },

    updateDeviceStatus: async (deviceId, status) => {
        const response = await fetch(`${API_BASE_URL}/devices/${deviceId}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ status })
        });
        return response.json();
    }
};