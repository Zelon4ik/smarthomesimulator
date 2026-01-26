package smarthomesimulator.presentation.forms;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.UserStoreDto;
import smarthomesimulator.presentation.AppContext;

public final class SignUpForm {
    public User run(AppContext ctx) {
        ctx.ui.hr();
        ctx.ui.headline("Реєстрація");

        String name = ctx.ui.prompt("Ім'я", true, String::trim, v -> v.trim().isEmpty() ? "Ім'я обов'язкове" : null);
        String email = ctx.ui.prompt("Email", true, s -> s.trim().toLowerCase(), v -> v.trim().isEmpty() ? "Email обов'язковий" : null);
        String password = ctx.ui.prompt("Пароль (мін. 8 символів)", true);

        return ctx.authenticationService.register(new UserStoreDto(name, email, password));
    }
}
