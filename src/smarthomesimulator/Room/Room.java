package smarthomesimulator.Room;

import smarthomesimulator.Device.Device;
import smarthomesimulator.ValidationUtils;

import java.util.List;

public class Room {
    private final String id;
    private final String name;
    private final List<Device> devices;

    public Room(String id, String name, List<Device> devices) {
        ValidationUtils.requireNotBlank(id, "Room ID");
        ValidationUtils.requireNotBlank(name, "Room name");
        ValidationUtils.requireNotNull(devices, "Devices list");

        this.id = id;
        this.name = name;
        this.devices = devices;
    }
}
