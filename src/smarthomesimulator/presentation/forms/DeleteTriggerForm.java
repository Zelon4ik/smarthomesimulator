package smarthomesimulator.presentation.forms;

import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class DeleteTriggerForm {
    public void run(AppContext ctx, Trigger trigger) {
        AccessControl.require(ctx.session, Permission.TRIGGERS_DELETE, "Видалити тригер");

        ctx.ui.hr();
        ctx.ui.headline("Видалити тригер");
        ctx.ui.warn("Ви збираєтеся видалити тригер:");
        ctx.ui.warn("ID: " + trigger.getId());
        ctx.ui.warn("Умова: " + trigger.getCondition());
        ctx.ui.warn("Дія: " + trigger.getAction());

        boolean ok = ctx.ui.confirm("Підтвердити видалення?", false);
        if (!ok) {
            ctx.ui.info("Скасовано.");
            return;
        }

        ctx.triggerService.deleteTrigger(trigger.getId());
        ctx.ui.success("Тригер видалено.");
    }
}
