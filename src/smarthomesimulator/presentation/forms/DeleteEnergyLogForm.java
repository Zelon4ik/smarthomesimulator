package smarthomesimulator.presentation.forms;

import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class DeleteEnergyLogForm {
    public void run(AppContext ctx, EnergyLog log) {
        AccessControl.require(ctx.session, Permission.ENERGYLOGS_DELETE, "Видалити журнал енергії");

        ctx.ui.hr();
        ctx.ui.headline("Видалити журнал енергії");
        ctx.ui.warn("Ви збираєтеся видалити журнал: " + log.getId());

        boolean ok = ctx.ui.confirm("Підтвердити видалення?", false);
        if (!ok) {
            ctx.ui.info("Скасовано.");
            return;
        }

        ctx.energyLogService.deleteEnergyLog(log.getId());
        ctx.ui.success("Журнал енергії видалено.");
    }
}
