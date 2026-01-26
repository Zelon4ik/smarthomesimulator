package smarthomesimulator.presentation.pages;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;
import smarthomesimulator.presentation.forms.DeleteSceneForm;
import smarthomesimulator.presentation.forms.EditSceneForm;

import java.util.ArrayList;
import java.util.List;

public final class SceneView implements View {
    private final String sceneId;

    public SceneView(String sceneId) {
        this.sceneId = sceneId;
    }

    @Override
    public View render(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.SCENES_READ, "Переглянути сцену");

        Scene scene = ctx.sceneService.findById(sceneId)
            .orElseThrow(() -> new IllegalArgumentException("Сцену не знайдено: " + sceneId));

        ctx.ui.hr();
        ctx.ui.headline("Сцена");

        System.out.println("ID: " + scene.getId());
        System.out.println("Назва: " + scene.getName());
        System.out.println("Пристрої:");
        List<Device> devices = scene.getDevices();
        if (devices == null || devices.isEmpty()) {
            System.out.println("  (немає)");
        } else {
            for (Device d : devices) {
                System.out.println("  - " + d.getName() + " (" + d.getType() + ") [" + (d.isOn() ? "УВІМКНЕНО" : "ВИМКНЕНО") + "]");
            }
        }

        List<String> menu = new ArrayList<>();
        if (AccessControl.can(ctx.session, Permission.SCENES_ACTIVATE)) menu.add("Активувати (увімкнути всі пристрої)");
        if (AccessControl.can(ctx.session, Permission.SCENES_ACTIVATE)) menu.add("Деактивувати (вимкнути всі пристрої)");
        if (AccessControl.can(ctx.session, Permission.SCENES_UPDATE)) menu.add("Редагувати");
        if (AccessControl.can(ctx.session, Permission.SCENES_DELETE)) menu.add("Видалити");
        menu.add("Назад");

        int choice = ctx.ui.select("Дії", menu);
        String action = menu.get(choice);

        switch (action) {
            case "Активувати (увімкнути всі пристрої)":
                AccessControl.require(ctx.session, Permission.SCENES_ACTIVATE, "Активувати сцену");
                ctx.sceneService.activateScene(scene.getId());
                ctx.ui.success("Сцену активовано.");
                return this;
            case "Деактивувати (вимкнути всі пристрої)":
                AccessControl.require(ctx.session, Permission.SCENES_ACTIVATE, "Деактивувати сцену");
                ctx.sceneService.deactivateScene(scene.getId());
                ctx.ui.success("Сцену деактивовано.");
                return this;
            case "Редагувати":
                new EditSceneForm().run(ctx, scene);
                return this;
            case "Видалити":
                new DeleteSceneForm().run(ctx, scene);
                return new ScenesView();
            default:
                return new ScenesView();
        }
    }
}
