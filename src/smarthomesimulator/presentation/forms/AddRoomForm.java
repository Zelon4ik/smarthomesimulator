package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Room.Room;
import smarthomesimulator.dto.RoomStoreDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

import java.util.List;
import java.util.stream.Collectors;

public final class AddRoomForm {
    public Room run(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.ROOMS_CREATE, "Додати кімнату");

        ctx.ui.hr();
        ctx.ui.headline("Додати кімнату");

        String name = ctx.ui.prompt("Назва", true, String::trim, v -> v.trim().isEmpty() ? "Назва обов'язкова" : null);

        List<Device> devices = ctx.deviceService.findAll();
        List<String> labels = devices.stream()
            .map(d -> d.getName() + " (" + d.getType() + ") [" + (d.isOn() ? "УВІМКНЕНО" : "ВИМКНЕНО") + "]")
            .collect(Collectors.toList());

        List<Integer> selected = ctx.ui.multiSelect("Виберіть пристрої для кімнати", labels);
        List<String> deviceIds = selected.stream().map(i -> devices.get(i).getId()).collect(Collectors.toList());

        Room room = ctx.roomService.createRoom(new RoomStoreDto(name, deviceIds));
        ctx.ui.success("Кімнату створено.");
        return room;
    }
}
