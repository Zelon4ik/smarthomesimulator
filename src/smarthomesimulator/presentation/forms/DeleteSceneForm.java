package smarthomesimulator.presentation.forms;

import smarthomesimulator.Scene.Scene;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

public final class DeleteSceneForm {
    public void run(AppContext ctx, Scene scene) {
        AccessControl.require(ctx.session, Permission.SCENES_DELETE, "Видалити сцену");

        ctx.ui.hr();
        ctx.ui.headline("Видалити сцену");
        ctx.ui.warn("Ви збираєтеся видалити: " + scene.getName());
        ctx.ui.warn("ID: " + scene.getId());

        boolean ok = ctx.ui.confirm("Підтвердити видалення?", false);
        if (!ok) {
            ctx.ui.info("Скасовано.");
            return;
        }

        ctx.sceneService.deleteScene(scene.getId());
        ctx.ui.success("Сцену видалено.");
    }
}
