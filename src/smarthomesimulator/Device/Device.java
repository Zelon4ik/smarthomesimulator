package smarthomesimulator.Device;

import smarthomesimulator.ValidationUtils;

public class Device {
    private final String id;
    private final String name;
    private final String type;
    private boolean isOn;
    private final double powerConsumption;

    public Device(String id, String name, String type, boolean isOn, double powerConsumption) {
        ValidationUtils.requireNotBlank(id, "Device ID");
        ValidationUtils.requireNotBlank(name, "Device name");
        ValidationUtils.requireNotBlank(type, "Device type");
        ValidationUtils.requirePositive(powerConsumption, "Power consumption");

        this.id = id;
        this.name = name;
        this.type = type;
        this.isOn = isOn;
        this.powerConsumption = powerConsumption;
    }
}
