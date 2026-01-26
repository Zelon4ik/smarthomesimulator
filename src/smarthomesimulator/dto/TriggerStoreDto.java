package smarthomesimulator.dto;

import smarthomesimulator.ValidationUtils;
import java.util.Objects;

public final class TriggerStoreDto {
    private final String condition;
    private final String action;

    public TriggerStoreDto(String condition, String action) {
        ValidationUtils.requireNotBlank(condition, "Condition");
        ValidationUtils.requireNotBlank(action, "Action");

        this.condition = condition.trim();
        this.action = action.trim();
    }

    public String getCondition() {
        return condition;
    }

    public String getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TriggerStoreDto that = (TriggerStoreDto) o;
        return Objects.equals(condition, that.condition) &&
               Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, action);
    }
}
