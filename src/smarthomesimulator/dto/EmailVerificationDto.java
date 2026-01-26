package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;

public final class EmailVerificationDto {
    private final String email;
    private final String token;

    public EmailVerificationDto(String email, String token) {
        ValidationUtils.requireNotBlank(email, "Email");
        ValidationUtils.requireNotBlank(token, "Verification token");

        this.email = email.trim().toLowerCase();
        this.token = token.trim();
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailVerificationDto that = (EmailVerificationDto) o;
        return Objects.equals(email, that.email) &&
               Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, token);
    }
}
