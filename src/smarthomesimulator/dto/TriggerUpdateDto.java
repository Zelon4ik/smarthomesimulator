package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;
import java.util.Optional;

public final class TriggerUpdateDto {
    private final String condition;
    private final String action;

    public TriggerUpdateDto(String condition, String action) {
        // Condition is optional but if provided must not be blank
        if (condition != null && condition.trim().isEmpty()) {
            throw new IllegalArgumentException("Condition cannot be empty");
        }
        this.condition = condition != null ? condition.trim() : null;

        // Action is optional but if provided must not be blank
        if (action != null && action.trim().isEmpty()) {
            throw new IllegalArgumentException("Action cannot be empty");
        }
        this.action = action != null ? action.trim() : null;
    }

    public Optional<String> getCondition() {
        return Optional.ofNullable(condition);
    }

    public Optional<String> getAction() {
        return Optional.ofNullable(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriggerUpdateDto that = (TriggerUpdateDto) o;
        return Objects.equals(condition, that.condition) &&
               Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, action);
    }
}
