package smarthomesimulator.EnergyLog;

import smarthomesimulator.ValidationUtils;

public class EnergyLog {
    private final String deviceId;
    private final double energyUsed;
    private final long timestamp;

    public EnergyLog(String deviceId, double energyUsed, long timestamp) {
        ValidationUtils.requireNotBlank(deviceId, "Device ID");
        ValidationUtils.requirePositive(energyUsed, "Energy used");

        this.deviceId = deviceId;
        this.energyUsed = energyUsed;
        this.timestamp = timestamp;
    }
}
