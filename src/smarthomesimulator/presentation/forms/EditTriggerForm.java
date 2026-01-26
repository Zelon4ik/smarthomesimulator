package smarthomesimulator.presentation.forms;

import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.dto.TriggerUpdateDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class EditTriggerForm {
    public Trigger run(AppContext ctx, Trigger existing) {
        AccessControl.require(ctx.session, Permission.TRIGGERS_UPDATE, "Редагувати тригер");

        ctx.ui.hr();
        ctx.ui.headline("Редагувати тригер");
        ctx.ui.info("ID: " + existing.getId());

        String condition = ctx.ui.promptOptional("Умова (порожнє = залишити)");
        String action = ctx.ui.promptOptional("Дія (порожнє = залишити)");

        TriggerUpdateDto dto = new TriggerUpdateDto(
            condition.trim().isEmpty() ? null : condition.trim(),
            action.trim().isEmpty() ? null : action.trim()
        );

        Trigger updated = ctx.triggerService.updateTrigger(existing.getId(), dto);
        ctx.ui.success("Тригер оновлено.");
        return updated;
    }
}
