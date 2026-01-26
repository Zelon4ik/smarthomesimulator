package smarthomesimulator.presentation.pages;

import smarthomesimulator.User.User;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.DeleteUserForm;
import smarthomesimulator.presentation.forms.EditUserForm;

import java.util.ArrayList;
import java.util.List;

public final class UserView implements View {
    private final String userId;

    public UserView(String userId) {
        this.userId = userId;
    }

    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.USERS_READ, "Переглянути користувача");

        User user = ctx.userService.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Користувача не знайдено: " + userId));

        ctx.ui.hr();
        ctx.ui.headline("Користувач");

        System.out.println("ID: " + user.getId());
        System.out.println("Ім'я: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Email підтверджено: " + user.isEmailVerified());
        System.out.println("Кімнати: " + (user.getRooms() == null ? 0 : user.getRooms().size()));

        List<String> menu = new ArrayList<>();
        if (AccessControl.can(ctx.session, Permission.USERS_UPDATE)) menu.add("Редагувати");
        if (AccessControl.can(ctx.session, Permission.USERS_DELETE)) menu.add("Видалити");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Редагувати": {
                new EditUserForm().run(ctx, user);
                return this;
            }
            case "Видалити": {
                new DeleteUserForm().run(ctx, user);
                return new UsersView();
            }
            default:
                return new UsersView();
        }
    }
}
