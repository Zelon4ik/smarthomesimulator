package smarthomesimulator.presentation.pages;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.EmailVerificationDto;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.EmailVerificationForm;
import smarthomesimulator.presentation.forms.SignUpForm;

public final class SignUpView implements View {
    @Override
    public View render(AppContext ctx) {
        ctx.ui.hr();
        ctx.ui.headline("Реєстрація");

        SignUpForm form = new SignUpForm();
        User user = form.run(ctx);

        ctx.ui.success("Реєстрацію виконано успішно.");
        ctx.ui.info("Email підтверджено: " + user.isEmailVerified());
        if (user.getEmailVerificationToken() != null) {
            ctx.ui.warn("Токен підтвердження (демо): " + user.getEmailVerificationToken());
        }

        boolean verifyNow = ctx.ui.confirm("Підтвердити email зараз?", true);
        if (verifyNow) {
            EmailVerificationForm verifyForm = new EmailVerificationForm();
            EmailVerificationDto dto = verifyForm.run(ctx, user.getEmail(), user.getEmailVerificationToken());
            boolean verified = ctx.authenticationService.verifyEmail(dto);
            if (verified) {
                ctx.ui.success("Email підтверджено.");
            }
        }

        return new AuthView();
    }
}
