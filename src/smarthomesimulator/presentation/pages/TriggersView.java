package smarthomesimulator.presentation.pages;

import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.AddTriggerForm;
import smarthomesimulator.presentation.forms.DeleteTriggerForm;
import smarthomesimulator.presentation.forms.EditTriggerForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TriggersView implements View {
    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.TRIGGERS_READ, "Переглянути тригери");

        ctx.ui.hr();
        ctx.ui.headline("Тригери");

        List<Trigger> triggers = ctx.triggerService.findAll();
        if (triggers.isEmpty()) {
            ctx.ui.warn("Тригерів не знайдено.");
        } else {
            for (int i = 0; i < triggers.size(); i++) {
                Trigger t = triggers.get(i);
                System.out.println(String.format("%2d  %s  ->  %s", i + 1, t.getCondition(), t.getAction()));
            }
        }

        List<String> menu = new ArrayList<>();
        menu.add("Відкрити тригер");
        if (AccessControl.can(ctx.session, Permission.TRIGGERS_CREATE)) menu.add("Додати тригер");
        if (AccessControl.can(ctx.session, Permission.TRIGGERS_UPDATE)) menu.add("Редагувати тригер");
        if (AccessControl.can(ctx.session, Permission.TRIGGERS_DELETE)) menu.add("Видалити тригер");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Відкрити тригер": {
                if (triggers.isEmpty()) {
                    ctx.ui.warn("Немає тригерів для відкриття.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть тригер",
                    triggers.stream().map(t -> t.getCondition() + " -> " + t.getAction()).collect(Collectors.toList())
                );
                return new TriggerView(triggers.get(idx).getId());
            }
            case "Додати тригер":
                new AddTriggerForm().run(ctx);
                return this;
            case "Редагувати тригер": {
                if (triggers.isEmpty()) {
                    ctx.ui.warn("Немає тригерів для редагування.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть тригер",
                    triggers.stream().map(t -> t.getCondition() + " -> " + t.getAction()).collect(Collectors.toList())
                );
                new EditTriggerForm().run(ctx, triggers.get(idx));
                return this;
            }
            case "Видалити тригер": {
                if (triggers.isEmpty()) {
                    ctx.ui.warn("Немає тригерів для видалення.");
                    return this;
                }
                int idx = ctx.ui.select(
                    "Виберіть тригер",
                    triggers.stream().map(t -> t.getCondition() + " -> " + t.getAction()).collect(Collectors.toList())
                );
                new DeleteTriggerForm().run(ctx, triggers.get(idx));
                return this;
            }
            default:
                return new MainMenuView();
        }
    }
}
