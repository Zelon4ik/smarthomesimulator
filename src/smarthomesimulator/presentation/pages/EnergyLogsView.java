package smarthomesimulator.presentation.pages;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.AddEnergyLogForm;
import smarthomesimulator.presentation.forms.DeleteEnergyLogForm;
import smarthomesimulator.presentation.forms.EditEnergyLogForm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class EnergyLogsView implements View {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.ENERGYLOGS_READ, "Переглянути журнали енергії");

        ctx.ui.hr();
        ctx.ui.headline("Журнали енергії");

        List<EnergyLog> logs = ctx.energyLogService.findAll();
        if (logs.isEmpty()) {
            ctx.ui.warn("Журналів енергії не знайдено.");
        } else {
            for (int i = 0; i < logs.size(); i++) {
                EnergyLog l = logs.get(i);
                Optional<Device> d = ctx.deviceService.findById(l.getDeviceId());
                String deviceName = d.map(Device::getName).orElse("Невідомий пристрій");
                LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(l.getTimestamp()), ZoneId.systemDefault());
                System.out.println(String.format("%2d  %s  %.2f кВт·год  %s", i + 1, deviceName, l.getEnergyUsed(), dt.format(FMT)));
            }
        }

        List<String> menu = new ArrayList<>();
        menu.add("Відкрити журнал");
        if (AccessControl.can(ctx.session, Permission.ENERGYLOGS_CREATE)) menu.add("Додати журнал");
        if (AccessControl.can(ctx.session, Permission.ENERGYLOGS_UPDATE)) menu.add("Редагувати журнал");
        if (AccessControl.can(ctx.session, Permission.ENERGYLOGS_DELETE)) menu.add("Видалити журнал");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Відкрити журнал": {
                if (logs.isEmpty()) {
                    ctx.ui.warn("Немає журналів для відкриття.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть журнал", logs.stream().map(EnergyLog::getId).collect(Collectors.toList()));
                return new EnergyLogView(logs.get(idx).getId());
            }
            case "Додати журнал":
                new AddEnergyLogForm().run(ctx);
                return this;
            case "Редагувати журнал": {
                if (logs.isEmpty()) {
                    ctx.ui.warn("Немає журналів для редагування.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть журнал", logs.stream().map(EnergyLog::getId).collect(Collectors.toList()));
                new EditEnergyLogForm().run(ctx, logs.get(idx));
                return this;
            }
            case "Видалити журнал": {
                if (logs.isEmpty()) {
                    ctx.ui.warn("Немає журналів для видалення.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть журнал", logs.stream().map(EnergyLog::getId).collect(Collectors.toList()));
                new DeleteEnergyLogForm().run(ctx, logs.get(idx));
                return this;
            }
            default:
                return new MainMenuView();
        }
    }
}
