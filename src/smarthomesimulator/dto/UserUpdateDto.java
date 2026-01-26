package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public final class UserUpdateDto {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final String name;
    private final String email;
    private final String password;

    public UserUpdateDto(String name, String email, String password) {
        // Name is optional but if provided must not be blank
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name != null ? name.trim() : null;

        // Email is optional but if provided must be valid
        if (email != null) {
            if (email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
        this.email = email != null ? email.trim().toLowerCase() : null;

        // Password is optional but if provided must meet requirements
        if (password != null) {
            if (password.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }
            if (password.length() < MIN_PASSWORD_LENGTH) {
                throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
            }
        }
        this.password = password;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserUpdateDto that = (UserUpdateDto) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
