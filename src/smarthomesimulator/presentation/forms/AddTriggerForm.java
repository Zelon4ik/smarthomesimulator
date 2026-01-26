package smarthomesimulator.presentation.forms;

import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.dto.TriggerStoreDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class AddTriggerForm {
    public Trigger run(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.TRIGGERS_CREATE, "Додати тригер");

        ctx.ui.hr();
        ctx.ui.headline("Додати тригер");

        String condition = ctx.ui.prompt("Умова", true, String::trim, v -> v.trim().isEmpty() ? "Умова обов'язкова" : null);
        String action = ctx.ui.prompt("Дія", true, String::trim, v -> v.trim().isEmpty() ? "Дія обов'язкова" : null);

        Trigger trigger = ctx.triggerService.createTrigger(new TriggerStoreDto(condition, action));
        ctx.ui.success("Тригер створено.");
        return trigger;
    }
}
