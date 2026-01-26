package smarthomesimulator.presentation.forms;

import smarthomesimulator.Room.Room;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class DeleteRoomForm {
    public void run(AppContext ctx, Room room) {
        AccessControl.require(ctx.session, Permission.ROOMS_DELETE, "Видалити кімнату");

        ctx.ui.hr();
        ctx.ui.headline("Видалити кімнату");
        ctx.ui.warn("Ви збираєтеся видалити: " + room.getName());
        ctx.ui.warn("ID: " + room.getId());

        boolean ok = ctx.ui.confirm("Підтвердити видалення?", false);
        if (!ok) {
            ctx.ui.info("Скасовано.");
            return;
        }

        ctx.roomService.deleteRoom(room.getId());
        ctx.ui.success("Кімнату видалено.");
    }
}
