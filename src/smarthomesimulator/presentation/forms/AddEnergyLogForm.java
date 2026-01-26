package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.dto.EnergyLogStoreDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

import java.util.List;
import java.util.stream.Collectors;

public final class AddEnergyLogForm {
    public EnergyLog run(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.ENERGYLOGS_CREATE, "Додати журнал енергії");

        ctx.ui.hr();
        ctx.ui.headline("Додати журнал енергії");

        List<Device> devices = ctx.deviceService.findAll();
        if (devices.isEmpty()) {
            throw new IllegalStateException("Пристроїв не існує. Спочатку створіть пристрій.");
        }

        int deviceIdx = ctx.ui.select(
            "Виберіть пристрій",
            devices.stream().map(d -> d.getName() + " (" + d.getType() + ")").collect(Collectors.toList())
        );
        String deviceId = devices.get(deviceIdx).getId();

        double energyUsed = ctx.ui.promptDouble("Використано енергії (кВт·год)", true, 0.0);

        long ts;
        boolean useNow = ctx.ui.confirm("Використати поточну мітку часу?", true);
        if (useNow) {
            ts = System.currentTimeMillis();
        } else {
            ts = ctx.ui.promptLong("Мітка часу (epoch мілісекунди)", true, 0L);
        }

        EnergyLog log = ctx.energyLogService.createEnergyLog(new EnergyLogStoreDto(deviceId, energyUsed, ts));
        ctx.ui.success("Журнал енергії створено.");
        return log;
    }
}
