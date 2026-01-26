package smarthomesimulator.presentation.pages;

import smarthomesimulator.Device.Device;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.DeleteDeviceForm;
import smarthomesimulator.presentation.forms.EditDeviceForm;

import java.util.ArrayList;
import java.util.List;

public final class DeviceView implements View {
    private final String deviceId;

    public DeviceView(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.DEVICES_READ, "Переглянути пристрій");

        Device device = ctx.deviceService.findById(deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено: " + deviceId));

        ctx.ui.hr();
        ctx.ui.headline("Пристрій");

        System.out.println("ID: " + device.getId());
        System.out.println("Назва: " + device.getName());
        System.out.println("Тип: " + device.getType());
        System.out.println("Стан: " + (device.isOn() ? "УВІМКНЕНО" : "ВИМКНЕНО"));
        System.out.println("Потужність: " + String.format("%.2f", device.getPowerConsumption()) + " Вт");

        List<String> menu = new ArrayList<>();
        if (AccessControl.can(ctx.session, Permission.DEVICES_TOGGLE)) menu.add("Перемкнути УВІМК/ВИМК");
        if (AccessControl.can(ctx.session, Permission.DEVICES_UPDATE)) menu.add("Редагувати");
        if (AccessControl.can(ctx.session, Permission.DEVICES_DELETE)) menu.add("Видалити");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Перемкнути УВІМК/ВИМК": {
                AccessControl.require(ctx.session, Permission.DEVICES_TOGGLE, "Перемкнути пристрій");
                Device updated = ctx.deviceService.toggleDevice(device.getId());
                ctx.ui.success("Пристрій тепер: " + (updated.isOn() ? "УВІМКНЕНО" : "ВИМКНЕНО"));
                return this;
            }
            case "Редагувати": {
                new EditDeviceForm().run(ctx, device);
                return this;
            }
            case "Видалити": {
                new DeleteDeviceForm().run(ctx, device);
                return new DevicesView();
            }
            default:
                return new DevicesView();
        }
    }
}
