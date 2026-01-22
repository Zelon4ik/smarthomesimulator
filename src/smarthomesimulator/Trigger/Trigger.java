package smarthomesimulator.Trigger;

import smarthomesimulator.ValidationUtils;

import java.util.UUID;

public class Trigger {
    private final String id;
    private String condition;
    private String action;

    public Trigger(String condition, String action) {
        this.id = UUID.randomUUID().toString();
        ValidationUtils.requireNotBlank(condition, "Condition");
        ValidationUtils.requireNotBlank(action, "Action");

        this.condition = condition;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        ValidationUtils.requireNotBlank(condition, "Condition");
        this.condition = condition;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        ValidationUtils.requireNotBlank(action, "Action");
        this.action = action;
    }
}
