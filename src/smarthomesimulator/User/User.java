package smarthomesimulator.User;

import smarthomesimulator.Room.Room;
import smarthomesimulator.ValidationUtils;
import java.util.List;
import java.util.UUID;

public class User {

    private final String id;
    private String name;
    private String email;
    private List<Room> rooms;

    public User(String name, String email, List<Room> rooms) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.rooms = rooms;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        ValidationUtils.requireNotBlank(name, "User name");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        ValidationUtils.requireNotBlank(email, "User email");
        this.email = email;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        ValidationUtils.requireNotNull(rooms, "Rooms list");
        this.rooms = rooms;
    }
}
