package smarthomesimulator.presentation;

import smarthomesimulator.User.User;

import java.util.Objects;
import java.util.Optional;

public final class Session {
    private User user;
    private Role role;

    public Session() {
        this.user = null;
        this.role = Role.GUEST;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public Optional<User> getUser() {
        return Optional.ofNullable(user);
    }

    public Role getRole() {
        return role;
    }

    public boolean has(Permission permission) {
        return role.has(permission);
    }

    public void login(User user, Role role) {
        this.user = Objects.requireNonNull(user, "user");
        this.role = Objects.requireNonNull(role, "role");
    }

    public void logout() {
        this.user = null;
        this.role = Role.GUEST;
    }
}

