package smarthomesimulator.presentation.forms;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.UserUpdateDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class EditUserForm {
    public User run(AppContext ctx, User existing) {
        AccessControl.require(ctx.session, Permission.USERS_UPDATE, "Редагувати користувача");

        ctx.ui.hr();
        ctx.ui.headline("Редагувати користувача");
        ctx.ui.info("Користувач: " + existing.getName() + " <" + existing.getEmail() + ">");
        ctx.ui.info("ID: " + existing.getId());

        String name = ctx.ui.promptOptional("Ім'я (порожнє = залишити)");
        String email = ctx.ui.promptOptional("Email (порожнє = залишити)");
        String password = ctx.ui.promptOptional("Новий пароль (порожнє = залишити)");

        UserUpdateDto dto = new UserUpdateDto(
            name.trim().isEmpty() ? null : name.trim(),
            email.trim().isEmpty() ? null : email.trim().toLowerCase(),
            password.isEmpty() ? null : password
        );
        User updated = ctx.userService.updateUser(existing.getId(), dto);
        ctx.ui.success("Користувача оновлено.");
        return updated;
    }
}
