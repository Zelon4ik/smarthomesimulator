package smarthomesimulator.Scene;

import smarthomesimulator.Device.Device;
import smarthomesimulator.ValidationUtils;

import java.util.List;
import java.util.UUID;

public class Scene {
    private final String id;
    private String name;
    private List<Device> devices;

    public Scene(String name, List<Device> devices) {
        this.id = UUID.randomUUID().toString();
        ValidationUtils.requireNotBlank(name, "Scene name");
        ValidationUtils.requireNotNull(devices, "Devices");

        this.name = name;
        this.devices = devices;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ValidationUtils.requireNotBlank(name, "Scene name");
        this.name = name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        ValidationUtils.requireNotNull(devices, "Devices");
        this.devices = devices;
    }
}
