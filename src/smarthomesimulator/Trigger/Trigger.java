package smarthomesimulator.Trigger;

import smarthomesimulator.ValidationUtils;

public class Trigger {
    private final String condition;
    private final String action;

    public Trigger(String condition, String action) {
        ValidationUtils.requireNotBlank(condition, "Condition");
        ValidationUtils.requireNotBlank(action, "Action");

        this.condition = condition;
        this.action = action;
    }
}
