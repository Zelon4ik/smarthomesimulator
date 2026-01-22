package smarthomesimulator.Room;

import smarthomesimulator.Device.Device;
import smarthomesimulator.ValidationUtils;

import java.util.List;

public class Room {
    private final String id;
    private String name;
    private List<Device> devices;

    public Room(String id, String name, List<Device> devices) {
        ValidationUtils.requireNotBlank(id, "Room ID");
        ValidationUtils.requireNotBlank(name, "Room name");
        ValidationUtils.requireNotNull(devices, "Devices list");

        this.id = id;
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
        ValidationUtils.requireNotBlank(name, "Room name");
        this.name = name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        ValidationUtils.requireNotNull(devices, "Devices list");
        this.devices = devices;
    }
}
