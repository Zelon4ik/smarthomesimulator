package smarthomesimulator.presentation.forms;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.UserStoreDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class AddUserForm {
    public User run(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.USERS_CREATE, "Додати користувача");

        ctx.ui.hr();
        ctx.ui.headline("Додати користувача");

        String name = ctx.ui.prompt("Ім'я", true, String::trim, v -> v.trim().isEmpty() ? "Ім'я обов'язкове" : null);
        String email = ctx.ui.prompt("Email", true, s -> s.trim().toLowerCase(), v -> v.trim().isEmpty() ? "Email обов'язковий" : null);
        String password = ctx.ui.prompt("Пароль (мін. 8 символів)", true);

        User user = ctx.authenticationService.register(new UserStoreDto(name, email, password));
        ctx.ui.success("Користувача створено.");
        if (user.getEmailVerificationToken() != null) {
            ctx.ui.warn("Токен підтвердження (демо): " + user.getEmailVerificationToken());
        }
        return user;
    }
}
