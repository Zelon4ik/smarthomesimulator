package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.dto.EnergyLogUpdateDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

import java.util.List;
import java.util.stream.Collectors;

public final class EditEnergyLogForm {
    public EnergyLog run(AppContext ctx, EnergyLog existing) {
        AccessControl.require(ctx.session, Permission.ENERGYLOGS_UPDATE, "Редагувати журнал енергії");

        ctx.ui.hr();
        ctx.ui.headline("Редагувати журнал енергії");
        ctx.ui.info("ID: " + existing.getId());

        String deviceId = null;
        boolean changeDevice = ctx.ui.confirm("Змінити пристрій?", false);
        if (changeDevice) {
            List<Device> devices = ctx.deviceService.findAll();
            if (devices.isEmpty()) {
                throw new IllegalStateException("Пристроїв не існує.");
            }
            int deviceIdx = ctx.ui.select(
                "Виберіть пристрій",
                devices.stream().map(d -> d.getName() + " (" + d.getType() + ")").collect(Collectors.toList())
            );
            deviceId = devices.get(deviceIdx).getId();
        }

        String energyRaw = ctx.ui.promptOptional("Використано енергії (кВт·год, порожнє = залишити)");
        Double energyUsed = null;
        if (!energyRaw.trim().isEmpty()) {
            try {
                energyUsed = Double.parseDouble(energyRaw.trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Невірне значення енергії.");
            }
        }

        String tsRaw = ctx.ui.promptOptional("Мітка часу epoch мілісекунди (порожнє = залишити)");
        Long ts = null;
        if (!tsRaw.trim().isEmpty()) {
            try {
                ts = Long.parseLong(tsRaw.trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Невірна мітка часу.");
            }
        }

        EnergyLogUpdateDto dto = new EnergyLogUpdateDto(deviceId, energyUsed, ts);
        EnergyLog updated = ctx.energyLogService.updateEnergyLog(existing.getId(), dto);
        ctx.ui.success("Журнал енергії оновлено.");
        return updated;
    }
}
