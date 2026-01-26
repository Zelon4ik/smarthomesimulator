package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.dto.DeviceUpdateDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class EditDeviceForm {
    public Device run(AppContext ctx, Device existing) {
        AccessControl.require(ctx.session, Permission.DEVICES_UPDATE, "Редагувати пристрій");

        ctx.ui.hr();
        ctx.ui.headline("Редагувати пристрій");
        ctx.ui.info("Пристрій: " + existing.getName() + " (" + existing.getType() + ")");
        ctx.ui.info("ID: " + existing.getId());

        String name = ctx.ui.promptOptional("Назва (порожнє = залишити)");
        String type = ctx.ui.promptOptional("Тип (порожнє = залишити)");
        String isOnRaw = ctx.ui.promptOptional("Увімкнено? (т/н, порожнє = залишити)");
        String powerRaw = ctx.ui.promptOptional("Споживання потужності (Вт, порожнє = залишити)");

        Boolean isOn = null;
        if (!isOnRaw.trim().isEmpty()) {
            String v = isOnRaw.trim().toLowerCase();
            if (v.equals("т") || v.equals("так") || v.equals("y") || v.equals("yes") || v.equals("true")) isOn = true;
            else if (v.equals("н") || v.equals("ні") || v.equals("n") || v.equals("no") || v.equals("false")) isOn = false;
            else throw new IllegalArgumentException("Невірне значення. Використовуйте т/н.");
        }

        Double power = null;
        if (!powerRaw.trim().isEmpty()) {
            try {
                power = Double.parseDouble(powerRaw.trim());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Невірне значення потужності.");
            }
        }

        DeviceUpdateDto dto = new DeviceUpdateDto(
            name.trim().isEmpty() ? null : name.trim(),
            type.trim().isEmpty() ? null : type.trim(),
            isOn,
            power
        );

        Device updated = ctx.deviceService.updateDevice(existing.getId(), dto);
        ctx.ui.success("Пристрій оновлено.");
        return updated;
    }
}
