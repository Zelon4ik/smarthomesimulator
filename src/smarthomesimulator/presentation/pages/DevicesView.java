package smarthomesimulator.presentation.pages;

import smarthomesimulator.Device.Device;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.AddDeviceForm;
import smarthomesimulator.presentation.forms.DeleteDeviceForm;
import smarthomesimulator.presentation.forms.EditDeviceForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DevicesView implements View {
    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.DEVICES_READ, "Переглянути пристрої");

        ctx.ui.hr();
        ctx.ui.headline("Пристрої");

        List<Device> devices = ctx.deviceService.findAll();
        if (devices.isEmpty()) {
            ctx.ui.warn("Пристроїв не знайдено.");
        } else {
            for (int i = 0; i < devices.size(); i++) {
                Device d = devices.get(i);
                String state = d.isOn() ? "УВІМКНЕНО" : "ВИМКНЕНО";
                System.out.println(String.format("%2d  %s (%s) [%s]  %.2f Вт", i + 1, d.getName(), d.getType(), state, d.getPowerConsumption()));
            }
        }

        List<String> menu = new ArrayList<>();
        menu.add("Відкрити пристрій");
        if (AccessControl.can(ctx.session, Permission.DEVICES_CREATE)) menu.add("Додати пристрій");
        if (AccessControl.can(ctx.session, Permission.DEVICES_UPDATE)) menu.add("Редагувати пристрій");
        if (AccessControl.can(ctx.session, Permission.DEVICES_DELETE)) menu.add("Видалити пристрій");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Відкрити пристрій": {
                if (devices.isEmpty()) {
                    ctx.ui.warn("Немає пристроїв для відкриття.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть пристрій",
                    devices.stream().map(d -> d.getName() + " (" + d.getType() + ")").collect(Collectors.toList())
                );
                return new DeviceView(devices.get(idx).getId());
            }
            case "Додати пристрій": {
                new AddDeviceForm().run(ctx);
                return this;
            }
            case "Редагувати пристрій": {
                if (devices.isEmpty()) {
                    ctx.ui.warn("Немає пристроїв для редагування.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть пристрій",
                    devices.stream().map(d -> d.getName() + " (" + d.getType() + ")").collect(Collectors.toList())
                );
                new EditDeviceForm().run(ctx, devices.get(idx));
                return this;
            }
            case "Видалити пристрій": {
                if (devices.isEmpty()) {
                    ctx.ui.warn("Немає пристроїв для видалення.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть пристрій",
                    devices.stream().map(d -> d.getName() + " (" + d.getType() + ")").collect(Collectors.toList())
                );
                new DeleteDeviceForm().run(ctx, devices.get(idx));
                return this;
            }
            default:
                return new MainMenuView();
        }
    }
}
