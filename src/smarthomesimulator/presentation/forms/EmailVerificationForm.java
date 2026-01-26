package smarthomesimulator.presentation.forms;

import smarthomesimulator.dto.EmailVerificationDto;
import smarthomesimulator.presentation.AppContext;

public final class EmailVerificationForm {
    public EmailVerificationDto run(AppContext ctx) {
        ctx.ui.hr();
        ctx.ui.headline("Підтвердити email");

        String email = ctx.ui.prompt("Email", true, s -> s.trim().toLowerCase(), v -> v.trim().isEmpty() ? "Email обов'язковий" : null);
        String token = ctx.ui.prompt("Токен підтвердження", true);
        return new EmailVerificationDto(email, token);
    }

    public EmailVerificationDto run(AppContext ctx, String defaultEmail, String defaultToken) {
        ctx.ui.hr();
        ctx.ui.headline("Підтвердити email");
        if (defaultEmail != null && !defaultEmail.trim().isEmpty()) {
            ctx.ui.info("Email за замовчуванням: " + defaultEmail);
        }
        if (defaultToken != null && !defaultToken.trim().isEmpty()) {
            ctx.ui.info("Токен за замовчуванням: " + defaultToken);
        }

        String emailInput = ctx.ui.promptOptional("Email (Enter для використання за замовчуванням)");
        String tokenInput = ctx.ui.promptOptional("Токен (Enter для використання за замовчуванням)");

        String email = (emailInput == null || emailInput.trim().isEmpty()) ? defaultEmail : emailInput.trim().toLowerCase();
        String token = (tokenInput == null || tokenInput.trim().isEmpty()) ? defaultToken : tokenInput.trim();

        return new EmailVerificationDto(email, token);
    }
}
