package smarthomesimulator.presentation.forms;

import smarthomesimulator.User.User;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class DeleteUserForm {
    public void run(AppContext ctx, User user) {
        AccessControl.require(ctx.session, Permission.USERS_DELETE, "Видалити користувача");

        ctx.ui.hr();
        ctx.ui.headline("Видалити користувача");
        ctx.ui.warn("Ви збираєтеся видалити: " + user.getName() + " <" + user.getEmail() + ">");
        ctx.ui.warn("ID: " + user.getId());

        boolean ok = ctx.ui.confirm("Підтвердити видалення?", false);
        if (!ok) {
            ctx.ui.info("Скасовано.");
            return;
        }

        ctx.userService.deleteUser(user.getId());
        ctx.ui.success("Користувача видалено.");
    }
}
