package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;

public final class EnergyLogStoreDto {
    private final String deviceId;
    private final double energyUsed;
    private final Long timestamp;

    public EnergyLogStoreDto(String deviceId, double energyUsed, Long timestamp) {
        ValidationUtils.requireNotBlank(deviceId, "Device ID");
        ValidationUtils.requirePositive(energyUsed, "Energy used");
        ValidationUtils.requireNotNull(timestamp, "Timestamp");

        this.deviceId = deviceId;
        this.energyUsed = energyUsed;
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public double getEnergyUsed() {
        return energyUsed;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnergyLogStoreDto that = (EnergyLogStoreDto) o;
        return Double.compare(that.energyUsed, energyUsed) == 0 &&
               Objects.equals(deviceId, that.deviceId) &&
               Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, energyUsed, timestamp);
    }
}
