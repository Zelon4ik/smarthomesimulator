package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class DeleteDeviceForm {
    public void run(AppContext ctx, Device device) {
        AccessControl.require(ctx.session, Permission.DEVICES_DELETE, "Видалити пристрій");

        ctx.ui.hr();
        ctx.ui.headline("Видалити пристрій");
        ctx.ui.warn("Ви збираєтеся видалити: " + device.getName() + " (" + device.getType() + ")");
        ctx.ui.warn("ID: " + device.getId());

        boolean ok = ctx.ui.confirm("Підтвердити видалення?", false);
        if (!ok) {
            ctx.ui.info("Скасовано.");
            return;
        }

        ctx.deviceService.deleteDevice(device.getId());
        ctx.ui.success("Пристрій видалено.");
    }
}
