package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.dto.DeviceStoreDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class AddDeviceForm {
    public Device run(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.DEVICES_CREATE, "Додати пристрій");

        ctx.ui.hr();
        ctx.ui.headline("Додати пристрій");

        String name = ctx.ui.prompt("Назва", true, String::trim, v -> v.trim().isEmpty() ? "Назва обов'язкова" : null);
        String type = ctx.ui.prompt("Тип", true, String::trim, v -> v.trim().isEmpty() ? "Тип обов'язковий" : null);
        boolean isOn = ctx.ui.confirm("Пристрій увімкнено?", true);
        double power = ctx.ui.promptDouble("Споживання потужності (Вт)", true, 0.0);

        Device device = ctx.deviceService.createDevice(new DeviceStoreDto(name, type, isOn, power));
        ctx.ui.success("Пристрій створено.");
        return device;
    }
}
