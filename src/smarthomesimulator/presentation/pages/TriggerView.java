package smarthomesimulator.presentation.pages;

import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.DeleteTriggerForm;
import smarthomesimulator.presentation.forms.EditTriggerForm;

import java.util.ArrayList;
import java.util.List;

public final class TriggerView implements View {
    private final String triggerId;

    public TriggerView(String triggerId) {
        this.triggerId = triggerId;
    }

    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.TRIGGERS_READ, "Переглянути тригер");

        Trigger trigger = ctx.triggerService.findById(triggerId)
            .orElseThrow(() -> new IllegalArgumentException("Тригер не знайдено: " + triggerId));

        ctx.ui.hr();
        ctx.ui.headline("Тригер");

        System.out.println("ID: " + trigger.getId());
        System.out.println("Умова: " + trigger.getCondition());
        System.out.println("Дія: " + trigger.getAction());

        List<String> menu = new ArrayList<>();
        if (AccessControl.can(ctx.session, Permission.TRIGGERS_UPDATE)) menu.add("Редагувати");
        if (AccessControl.can(ctx.session, Permission.TRIGGERS_DELETE)) menu.add("Видалити");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Редагувати":
                new EditTriggerForm().run(ctx, trigger);
                return this;
            case "Видалити":
                new DeleteTriggerForm().run(ctx, trigger);
                return new TriggersView();
            default:
                return new TriggersView();
        }
    }
}
