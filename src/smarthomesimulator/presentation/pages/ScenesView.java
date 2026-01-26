package smarthomesimulator.presentation.pages;

import smarthomesimulator.Scene.Scene;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.AddSceneForm;
import smarthomesimulator.presentation.forms.DeleteSceneForm;
import smarthomesimulator.presentation.forms.EditSceneForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ScenesView implements View {
    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.SCENES_READ, "Переглянути сцени");

        ctx.ui.hr();
        ctx.ui.headline("Сцени");

        List<Scene> scenes = ctx.sceneService.findAll();
        if (scenes.isEmpty()) {
            ctx.ui.warn("Сцен не знайдено.");
        } else {
            for (int i = 0; i < scenes.size(); i++) {
                Scene s = scenes.get(i);
                int deviceCount = s.getDevices() == null ? 0 : s.getDevices().size();
                System.out.println(String.format("%2d  %s  (%d пристроїв)", i + 1, s.getName(), deviceCount));
            }
        }

        List<String> menu = new ArrayList<>();
        menu.add("Відкрити сцену");
        if (AccessControl.can(ctx.session, Permission.SCENES_CREATE)) menu.add("Додати сцену");
        if (AccessControl.can(ctx.session, Permission.SCENES_UPDATE)) menu.add("Редагувати сцену");
        if (AccessControl.can(ctx.session, Permission.SCENES_DELETE)) menu.add("Видалити сцену");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Відкрити сцену": {
                if (scenes.isEmpty()) {
                    ctx.ui.warn("Немає сцен для відкриття.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть сцену", scenes.stream().map(Scene::getName).collect(Collectors.toList()));
                return new SceneView(scenes.get(idx).getId());
            }
            case "Додати сцену": {
                new AddSceneForm().run(ctx);
                return this;
            }
            case "Редагувати сцену": {
                if (scenes.isEmpty()) {
                    ctx.ui.warn("Немає сцен для редагування.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть сцену", scenes.stream().map(Scene::getName).collect(Collectors.toList()));
                new EditSceneForm().run(ctx, scenes.get(idx));
                return this;
            }
            case "Видалити сцену": {
                if (scenes.isEmpty()) {
                    ctx.ui.warn("Немає сцен для видалення.");
                    return this;
                }
                int idx = ctx.ui.select("Виберіть сцену", scenes.stream().map(Scene::getName).collect(Collectors.toList()));
                new DeleteSceneForm().run(ctx, scenes.get(idx));
                return this;
            }
            default:
                return new MainMenuView();
        }
    }
}
