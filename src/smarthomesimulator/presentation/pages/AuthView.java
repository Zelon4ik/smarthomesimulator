package smarthomesimulator.presentation.pages;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.EmailVerificationDto;
import smarthomesimulator.dto.UserStoreDto;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Console;
import smarthomesimulator.presentation.Role;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.EmailVerificationForm;
import smarthomesimulator.presentation.forms.LoginForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AuthView implements View {
    @Override
    public View render(AppContext ctx) throws Exception {
        ctx.ui.hr();
        ctx.ui.headline("Автентифікація");

        Optional<User> currentUser = ctx.session.getUser();
        if (currentUser.isPresent()) {
            User u = currentUser.get();
            ctx.ui.info("Вхід виконано як: " + u.getName() + " <" + u.getEmail() + ">");
            ctx.ui.info("Роль: " + ctx.session.getRole());
            ctx.ui.hr();

            List<String> options = List.of(
                "Продовжити",
                "Вийти",
                "Завершити"
            );
            int c = ctx.ui.select("Меню", options);
            switch (c) {
                case 0:
                    return new MainMenuView();
                case 1:
                    ctx.session.logout();
                    ctx.ui.success("Вихід виконано.");
                    return this;
                default:
                    return null;
            }
        }

        List<String> options = new ArrayList<>();
        options.add("Вхід");
        options.add("Реєстрація");
        options.add("Підтвердити email");
        options.add("Надіслати повторно email підтвердження");
        options.add("Створити демо-адміністратора");
        options.add("Завершити");

        int choice = ctx.ui.select("Меню", options);
        switch (choice) {
            case 0: {
                LoginForm loginForm = new LoginForm();
                if (loginForm.run(ctx).isPresent()) {
                    ctx.ui.success("Вхід виконано.");
                    return new MainMenuView();
                }
                return this;
            }
            case 1:
                return new SignUpView();
            case 2: {
                EmailVerificationForm form = new EmailVerificationForm();
                EmailVerificationDto dto = form.run(ctx);
                boolean verified = ctx.authenticationService.verifyEmail(dto);
                if (verified) {
                    ctx.ui.success("Email успішно підтверджено.");
                }
                return this;
            }
            case 3: {
                String email = ctx.ui.prompt("Email", true, s -> s.trim().toLowerCase(), v -> v.trim().isEmpty() ? "Email обов'язковий" : null);
                ctx.authenticationService.resendVerificationEmail(email);
                ctx.ui.success("Email підтвердження надіслано повторно (дивіться вивід консолі).");
                return this;
            }
            case 4: {
                ctx.ui.hr();
                ctx.ui.headline("Створення демо-адміністратора");
                ctx.ui.info("Це створить обліковий запис адміністратора для демонстрації.");
                ctx.ui.info("Email має починатися з 'admin' або закінчуватися на '@admin.com' для отримання ролі адміністратора.");
                
                String name = ctx.ui.prompt("Ім'я", true, String::trim, v -> v.trim().isEmpty() ? "Ім'я обов'язкове" : null);
                String email = ctx.ui.prompt("Email (для адміністратора)", true, s -> s.trim().toLowerCase(), v -> v.trim().isEmpty() ? "Email обов'язковий" : null);
                String password = ctx.ui.prompt("Пароль (мін. 8 символів)", true);
                
                try {
                    User adminUser = ctx.authenticationService.register(new UserStoreDto(name, email, password));
                    adminUser.setEmailVerified(true);
                    ctx.userRepository.update(adminUser);
                    ctx.ui.success("Демо-адміністратора створено.");
                    ctx.ui.info("Email автоматично підтверджено для демонстрації.");
                    ctx.ui.info("Роль: " + ctx.inferRoleFromEmail(email));
                } catch (IllegalArgumentException iae) {
                    ctx.ui.error(iae.getMessage());
                }
                return this;
            }
            default:
                Console.plain("");
                return null;
        }
    }
}
