package smarthomesimulator.Scene;

import smarthomesimulator.Device.Device;
import smarthomesimulator.ValidationUtils;

import java.util.List;

public class Scene {
    private final String name;
    private final List<Device> devices;

    public Scene(String name, List<Device> devices) {
        ValidationUtils.requireNotBlank(name, "Scene name");
        ValidationUtils.requireNotNull(devices, "Devices");

        this.name = name;
        this.devices = devices;
    }
}
