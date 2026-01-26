package smarthomesimulator.presentation.forms;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.LoginDto;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Role;

import java.util.Optional;

public final class LoginForm {
    public Optional<User> run(AppContext ctx) {
        ctx.ui.hr();
        ctx.ui.headline("Вхід");

        String email = ctx.ui.prompt("Email", true, s -> s.trim().toLowerCase(), v -> v.trim().isEmpty() ? "Email обов'язковий" : null);
        String password = ctx.ui.prompt("Пароль", true);

        try {
            Optional<User> userOpt = ctx.authenticationService.login(new LoginDto(email, password));
            if (userOpt.isEmpty()) {
                ctx.ui.error("Невірний email або пароль.");
                return Optional.empty();
            }

            User user = userOpt.get();
            Role role = ctx.inferRoleFromEmail(user.getEmail());
            ctx.session.login(user, role);
            return Optional.of(user);
        } catch (IllegalStateException ise) {
            ctx.ui.error(ise.getMessage());
            return Optional.empty();
        }
    }
}
