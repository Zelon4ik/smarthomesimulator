package smarthomesimulator.User;

import smarthomesimulator.Room.Room;
import smarthomesimulator.ValidationUtils;

import java.util.List;

public class User {
    private final String id;
    private final String name;
    private final List<Room> rooms;

    public User(String id, String name, List<Room> rooms) {
        ValidationUtils.requireNotBlank(id, "User ID");
        ValidationUtils.requireNotBlank(name, "User name");
        ValidationUtils.requireNotNull(rooms, "Rooms");

        this.id = id;
        this.name = name;
        this.rooms = rooms;
    }
}
