package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;

public final class LoginDto {
    private final String email;
    private final String password;

    public LoginDto(String email, String password) {
        ValidationUtils.requireNotBlank(email, "Email");
        ValidationUtils.requireNotBlank(password, "Password");

        this.email = email.trim().toLowerCase();
        this.password = password;
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
        LoginDto loginDto = (LoginDto) o;
        return Objects.equals(email, loginDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
