package smarthomesimulator.User;

import smarthomesimulator.Room.Room;
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
}
