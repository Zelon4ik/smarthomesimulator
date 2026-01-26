package smarthomesimulator.User;

import smarthomesimulator.Room.Room;
import smarthomesimulator.ValidationUtils;
import java.util.List;
import java.util.UUID;

public class User {

    private final String id;
    private String name;
    private String email;
    private String passwordHash;
    private boolean emailVerified;
    private String emailVerificationToken;
    private List<Room> rooms;

    public User(String name, String email, String passwordHash, List<Room> rooms) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.emailVerified = false;
        this.emailVerificationToken = null;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        ValidationUtils.requireNotBlank(passwordHash, "Password hash");
        this.passwordHash = passwordHash;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        ValidationUtils.requireNotNull(rooms, "Rooms list");
        this.rooms = rooms;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }
}
