package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;
import java.util.Optional;

public final class DeviceUpdateDto {
    private final String name;
    private final String type;
    private final Boolean isOn;
    private final Double powerConsumption;

    public DeviceUpdateDto(String name, String type, Boolean isOn, Double powerConsumption) {
        // Name is optional but if provided must not be blank
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("Device name cannot be empty");
        }
        this.name = name != null ? name.trim() : null;

        // Type is optional but if provided must not be blank
        if (type != null && type.trim().isEmpty()) {
            throw new IllegalArgumentException("Device type cannot be empty");
        }
        this.type = type != null ? type.trim() : null;

        this.isOn = isOn;

        // Power consumption is optional but if provided must be positive
        if (powerConsumption != null) {
            ValidationUtils.requirePositive(powerConsumption, "Power consumption");
        }
        this.powerConsumption = powerConsumption;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<Boolean> getIsOn() {
        return Optional.ofNullable(isOn);
    }

    public Optional<Double> getPowerConsumption() {
        return Optional.ofNullable(powerConsumption);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceUpdateDto that = (DeviceUpdateDto) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(type, that.type) &&
               Objects.equals(isOn, that.isOn) &&
               Objects.equals(powerConsumption, that.powerConsumption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isOn, powerConsumption);
    }
}
