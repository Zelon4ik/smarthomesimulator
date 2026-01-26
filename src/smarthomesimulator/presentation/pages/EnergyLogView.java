package smarthomesimulator.presentation.pages;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.DeleteEnergyLogForm;
import smarthomesimulator.presentation.forms.EditEnergyLogForm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class EnergyLogView implements View {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String logId;

    public EnergyLogView(String logId) {
        this.logId = logId;
    }

    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.ENERGYLOGS_READ, "Переглянути журнал енергії");

        EnergyLog log = ctx.energyLogService.findById(logId)
            .orElseThrow(() -> new IllegalArgumentException("Журнал енергії не знайдено: " + logId));

        Optional<Device> deviceOpt = ctx.deviceService.findById(log.getDeviceId());
        String deviceName = deviceOpt.map(Device::getName).orElse("Невідомий пристрій");

        LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(log.getTimestamp()), ZoneId.systemDefault());

        ctx.ui.hr();
        ctx.ui.headline("Журнал енергії");

        System.out.println("ID: " + log.getId());
        System.out.println("Пристрій: " + deviceName + " (ID: " + log.getDeviceId() + ")");
        System.out.println("Використано енергії: " + String.format("%.2f", log.getEnergyUsed()) + " кВт·год");
        System.out.println("Мітка часу: " + log.getTimestamp());
        System.out.println("Дата: " + dt.format(FMT));

        List<String> menu = new ArrayList<>();
        if (AccessControl.can(ctx.session, Permission.ENERGYLOGS_UPDATE)) menu.add("Редагувати");
        if (AccessControl.can(ctx.session, Permission.ENERGYLOGS_DELETE)) menu.add("Видалити");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Редагувати":
                new EditEnergyLogForm().run(ctx, log);
                return this;
            case "Видалити":
                new DeleteEnergyLogForm().run(ctx, log);
                return new EnergyLogsView();
            default:
                return new EnergyLogsView();
        }
    }
}
