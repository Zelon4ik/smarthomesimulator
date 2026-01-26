package smarthomesimulator.presentation.pages;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Room.Room;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.DeleteRoomForm;
import smarthomesimulator.presentation.forms.EditRoomForm;

import java.util.ArrayList;
import java.util.List;

public final class RoomView implements View {
    private final String roomId;

    public RoomView(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.ROOMS_READ, "Переглянути кімнату");

        Room room = ctx.roomService.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Кімнату не знайдено: " + roomId));

        ctx.ui.hr();
        ctx.ui.headline("Кімната");

        System.out.println("ID: " + room.getId());
        System.out.println("Назва: " + room.getName());
        System.out.println("Пристрої:");
        List<Device> devices = room.getDevices();
        if (devices == null || devices.isEmpty()) {
            System.out.println("  (немає)");
        } else {
            for (Device d : devices) {
                System.out.println("  - " + d.getName() + " (" + d.getType() + ") [" + (d.isOn() ? "УВІМКНЕНО" : "ВИМКНЕНО") + "]");
            }
        }

        List<String> menu = new ArrayList<>();
        if (AccessControl.can(ctx.session, Permission.ROOMS_UPDATE)) menu.add("Редагувати");
        if (AccessControl.can(ctx.session, Permission.ROOMS_DELETE)) menu.add("Видалити");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Редагувати":
                new EditRoomForm().run(ctx, room);
                return this;
            case "Видалити":
                new DeleteRoomForm().run(ctx, room);
                return new RoomsView();
            default:
                return new RoomsView();
        }
    }
}
