package smarthomesimulator.presentation.pages;

import smarthomesimulator.User.User;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.AddUserForm;
import smarthomesimulator.presentation.forms.DeleteUserForm;
import smarthomesimulator.presentation.forms.EditUserForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class UsersView implements View {
    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.USERS_READ, "Переглянути користувачів");

        ctx.ui.hr();
        ctx.ui.headline("Користувачі");

        List<User> users = ctx.userService.findAll();
        if (users.isEmpty()) {
            ctx.ui.warn("Користувачів не знайдено.");
        } else {
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                String verified = u.isEmailVerified() ? "підтверджено" : "не підтверджено";
                System.out.println(String.format("%2d  %s <%s>  (%s)", i + 1, u.getName(), u.getEmail(), verified));
            }
        }

        List<String> menu = new ArrayList<>();
        menu.add("Відкрити користувача");
        if (AccessControl.can(ctx.session, Permission.USERS_CREATE)) menu.add("Додати користувача");
        if (AccessControl.can(ctx.session, Permission.USERS_UPDATE)) menu.add("Редагувати користувача");
        if (AccessControl.can(ctx.session, Permission.USERS_DELETE)) menu.add("Видалити користувача");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Відкрити користувача": {
                if (users.isEmpty()) {
                    ctx.ui.warn("Немає користувачів для відкриття.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть користувача",
                    users.stream().map(u -> u.getName() + " <" + u.getEmail() + ">").collect(Collectors.toList())
                );
                return new UserView(users.get(idx).getId());
            }
            case "Додати користувача": {
                new AddUserForm().run(ctx);
                return this;
            }
            case "Редагувати користувача": {
                if (users.isEmpty()) {
                    ctx.ui.warn("Немає користувачів для редагування.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть користувача",
                    users.stream().map(u -> u.getName() + " <" + u.getEmail() + ">").collect(Collectors.toList())
                );
                User target = users.get(idx);
                new EditUserForm().run(ctx, target);
                return this;
            }
            case "Видалити користувача": {
                if (users.isEmpty()) {
                    ctx.ui.warn("Немає користувачів для видалення.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть користувача",
                    users.stream().map(u -> u.getName() + " <" + u.getEmail() + ">").collect(Collectors.toList())
                );
                User target = users.get(idx);
                new DeleteUserForm().run(ctx, target);
                return this;
            }
            default:
                return new MainMenuView();
        }
    }
}
