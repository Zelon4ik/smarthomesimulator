package smarthomesimulator.Device;

import smarthomesimulator.ValidationUtils;

public class Device {
    private final String id;
    private String name;
    private String type;
    private boolean isOn;
    private double powerConsumption;

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ValidationUtils.requireNotBlank(name, "Device name");
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        ValidationUtils.requireNotBlank(type, "Device type");
        this.type = type;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public double getPowerConsumption() {
        return powerConsumption;
    }

    public void setPowerConsumption(double powerConsumption) {
        ValidationUtils.requirePositive(powerConsumption, "Power consumption");
        this.powerConsumption = powerConsumption;
    }
}
