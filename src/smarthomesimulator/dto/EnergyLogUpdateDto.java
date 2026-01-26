package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;
import java.util.Optional;

public final class EnergyLogUpdateDto {
    private final String deviceId;
    private final Double energyUsed;
    private final Long timestamp;

    public EnergyLogUpdateDto(String deviceId, Double energyUsed, Long timestamp) {
        // Device ID is optional but if provided must not be blank
        if (deviceId != null && deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Device ID cannot be empty");
        }
        this.deviceId = deviceId != null ? deviceId.trim() : null;

        // Energy used is optional but if provided must be positive
        if (energyUsed != null) {
            ValidationUtils.requirePositive(energyUsed, "Energy used");
        }
        this.energyUsed = energyUsed;

        this.timestamp = timestamp;
    }

    public Optional<String> getDeviceId() {
        return Optional.ofNullable(deviceId);
    }

    public Optional<Double> getEnergyUsed() {
        return Optional.ofNullable(energyUsed);
    }

    public Optional<Long> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnergyLogUpdateDto that = (EnergyLogUpdateDto) o;
        return Objects.equals(deviceId, that.deviceId) &&
               Objects.equals(energyUsed, that.energyUsed) &&
               Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, energyUsed, timestamp);
    }
}
