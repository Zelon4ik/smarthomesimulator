package smarthomesimulator.presentation.pages;

import smarthomesimulator.User.User;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Console;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class MainMenuView implements View {

    private static final class Option {
        final String label;
        final Supplier<View> next;

        Option(String label, Supplier<View> next) {
            this.label = label;
            this.next = next;
        }
    }

    @Override
    public View render(AppContext ctx) {
        ctx.ui.hr();
        ctx.ui.headline("Головне меню");

        Optional<User> u = ctx.session.getUser();
        if (u.isPresent()) {
            ctx.ui.info("Користувач: " + u.get().getName() + " <" + u.get().getEmail() + ">");
            ctx.ui.info("Роль: " + ctx.session.getRole());
        } else {
            ctx.ui.warn("Не автентифіковано. Повернення до автентифікації.");
            return new AuthView();
        }

        List<Option> options = new ArrayList<>();

        if (AccessControl.can(ctx.session, Permission.USERS_READ)) {
            options.add(new Option("Користувачі", UsersView::new));
        }
        if (AccessControl.can(ctx.session, Permission.DEVICES_READ)) {
            options.add(new Option("Пристрої", DevicesView::new));
        }
        if (AccessControl.can(ctx.session, Permission.ROOMS_READ)) {
            options.add(new Option("Кімнати", RoomsView::new));
        }
        if (AccessControl.can(ctx.session, Permission.SCENES_READ)) {
            options.add(new Option("Сцени", ScenesView::new));
        }
        if (AccessControl.can(ctx.session, Permission.TRIGGERS_READ)) {
            options.add(new Option("Тригери", TriggersView::new));
        }
        if (AccessControl.can(ctx.session, Permission.ENERGYLOGS_READ)) {
            options.add(new Option("Журнали енергії", EnergyLogsView::new));
        }
        if (AccessControl.can(ctx.session, Permission.DOCUMENTS_EXPORT)) {
            options.add(new Option("Документи / Експорт", DocumentsView::new));
        }

        options.add(new Option("Вийти", () -> {
            ctx.session.logout();
            ctx.ui.success("Вихід виконано.");
            return new AuthView();
        }));
        options.add(new Option("Завершити", () -> null));

        List<String> labels = options.stream().map(o -> o.label).collect(Collectors.toList());
        int choice = ctx.ui.select("Навігація", labels);
        try {
            return options.get(choice).next.get();
        } catch (Exception ex) {
            Console.error("Не вдалося відкрити: " + ex.getMessage());
            return this;
        }
    }
}
