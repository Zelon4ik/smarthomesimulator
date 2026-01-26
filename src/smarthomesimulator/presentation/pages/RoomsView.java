package smarthomesimulator.presentation.pages;

import smarthomesimulator.Room.Room;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.AddRoomForm;
import smarthomesimulator.presentation.forms.DeleteRoomForm;
import smarthomesimulator.presentation.forms.EditRoomForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RoomsView implements View {
    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.ROOMS_READ, "Переглянути кімнати");

        ctx.ui.hr();
        ctx.ui.headline("Кімнати");

        List<Room> rooms = ctx.roomService.findAll();
        if (rooms.isEmpty()) {
            ctx.ui.warn("Кімнат не знайдено.");
        } else {
            for (int i = 0; i < rooms.size(); i++) {
                Room r = rooms.get(i);
                int deviceCount = r.getDevices() == null ? 0 : r.getDevices().size();
                System.out.println(String.format("%2d  %s  (%d пристроїв)", i + 1, r.getName(), deviceCount));
            }
        }

        List<String> menu = new ArrayList<>();
        menu.add("Відкрити кімнату");
        if (AccessControl.can(ctx.session, Permission.ROOMS_CREATE)) menu.add("Додати кімнату");
        if (AccessControl.can(ctx.session, Permission.ROOMS_UPDATE)) menu.add("Редагувати кімнату");
        if (AccessControl.can(ctx.session, Permission.ROOMS_DELETE)) menu.add("Видалити кімнату");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Відкрити кімнату": {
                if (rooms.isEmpty()) {
                    ctx.ui.warn("Немає кімнат для відкриття.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть кімнату", rooms.stream().map(Room::getName).collect(Collectors.toList()));
                return new RoomView(rooms.get(idx).getId());
            }
            case "Додати кімнату": {
                new AddRoomForm().run(ctx);
                return this;
            }
            case "Редагувати кімнату": {
                if (rooms.isEmpty()) {
                    ctx.ui.warn("Немає кімнат для редагування.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть кімнату", rooms.stream().map(Room::getName).collect(Collectors.toList()));
                new EditRoomForm().run(ctx, rooms.get(idx));
                return this;
            }
            case "Видалити кімнату": {
                if (rooms.isEmpty()) {
                    ctx.ui.warn("Немає кімнат для видалення.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть кімнату", rooms.stream().map(Room::getName).collect(Collectors.toList()));
                new DeleteRoomForm().run(ctx, rooms.get(idx));
                return this;
            }
            default:
                return new MainMenuView();
        }
    }
}
