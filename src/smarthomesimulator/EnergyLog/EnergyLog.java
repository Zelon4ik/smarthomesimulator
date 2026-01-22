package smarthomesimulator.EnergyLog;

import smarthomesimulator.ValidationUtils;

import java.util.UUID;

public class EnergyLog {
    private final String id;
    private String deviceId;
    private double energyUsed;
    private long timestamp;

    public EnergyLog(String deviceId, double energyUsed, long timestamp) {
        this.id = UUID.randomUUID().toString();
        ValidationUtils.requireNotBlank(deviceId, "Device ID");
        ValidationUtils.requirePositive(energyUsed, "Energy used");

        this.deviceId = deviceId;
        this.energyUsed = energyUsed;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        ValidationUtils.requireNotBlank(deviceId, "Device ID");
        this.deviceId = deviceId;
    }

    public double getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(double energyUsed) {
        ValidationUtils.requirePositive(energyUsed, "Energy used");
        this.energyUsed = energyUsed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
