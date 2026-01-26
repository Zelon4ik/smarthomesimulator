package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Room.Room;
import smarthomesimulator.dto.RoomUpdateDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

import java.util.List;
import java.util.stream.Collectors;

public final class EditRoomForm {
    public Room run(AppContext ctx, Room existing) {
        AccessControl.require(ctx.session, Permission.ROOMS_UPDATE, "Редагувати кімнату");

        ctx.ui.hr();
        ctx.ui.headline("Редагувати кімнату");
        ctx.ui.info("Кімната: " + existing.getName());
        ctx.ui.info("ID: " + existing.getId());

        String name = ctx.ui.promptOptional("Назва (порожнє = залишити)");

        List<String> deviceIds = null;
        boolean changeDevices = ctx.ui.confirm("Змінити список пристроїв?", false);
        if (changeDevices) {
            List<Device> devices = ctx.deviceService.findAll();
            List<String> labels = devices.stream()
                .map(d -> d.getName() + " (" + d.getType() + ")")
                .collect(Collectors.toList());
            List<Integer> selected = ctx.ui.multiSelect("Виберіть пристрої", labels);
            deviceIds = selected.stream().map(i -> devices.get(i).getId()).collect(Collectors.toList());
        }

        RoomUpdateDto dto = new RoomUpdateDto(
            name.trim().isEmpty() ? null : name.trim(),
            deviceIds
        );

        Room updated = ctx.roomService.updateRoom(existing.getId(), dto);
        ctx.ui.success("Кімнату оновлено.");
        return updated;
    }
}
