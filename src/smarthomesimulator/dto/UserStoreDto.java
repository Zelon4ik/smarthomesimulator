package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;
import java.util.regex.Pattern;

public final class UserStoreDto {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final String name;
    private final String email;
    private final String password;

    public UserStoreDto(String name, String email, String password) {
        ValidationUtils.requireNotBlank(name, "Name");
        ValidationUtils.requireNotBlank(email, "Email");
        ValidationUtils.requireNotBlank(password, "Password");

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }

        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStoreDto that = (UserStoreDto) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
