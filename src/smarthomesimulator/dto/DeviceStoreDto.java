package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;

public final class DeviceStoreDto {
    private final String name;
    private final String type;
    private final boolean isOn;
    private final double powerConsumption;

    public DeviceStoreDto(String name, String type, boolean isOn, double powerConsumption) {
        ValidationUtils.requireNotBlank(name, "Device name");
        ValidationUtils.requireNotBlank(type, "Device type");
        ValidationUtils.requirePositive(powerConsumption, "Power consumption");

        this.name = name.trim();
        this.type = type.trim();
        this.isOn = isOn;
        this.powerConsumption = powerConsumption;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isOn() {
        return isOn;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceStoreDto that = (DeviceStoreDto) o;
        return isOn == that.isOn &&
               Double.compare(that.powerConsumption, powerConsumption) == 0 &&
               Objects.equals(name, that.name) &&
               Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, isOn, powerConsumption);
    }
}
