package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.dto.SceneUpdateDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

import java.util.List;
import java.util.stream.Collectors;

public final class EditSceneForm {
    public Scene run(AppContext ctx, Scene existing) {
        AccessControl.require(ctx.session, Permission.SCENES_UPDATE, "Редагувати сцену");

        ctx.ui.hr();
        ctx.ui.headline("Редагувати сцену");
        ctx.ui.info("Сцена: " + existing.getName());
        ctx.ui.info("ID: " + existing.getId());

        String name = ctx.ui.promptOptional("Назва (порожнє = залишити)");

        List<String> deviceIds = null;
        boolean changeDevices = ctx.ui.confirm("Змінити список пристроїв?", false);
        if (changeDevices) {
            List<Device> devices = ctx.deviceService.findAll();
            List<String> labels = devices.stream()
                .map(d -> d.getName() + " (" + d.getType() + ")")
                .collect(Collectors.toList());
            List<Integer> selected = ctx.ui.multiSelect("Виберіть пристрої", labels);
            deviceIds = selected.stream().map(i -> devices.get(i).getId()).collect(Collectors.toList());
        }

        SceneUpdateDto dto = new SceneUpdateDto(
            name.trim().isEmpty() ? null : name.trim(),
            deviceIds
        );

        Scene updated = ctx.sceneService.updateScene(existing.getId(), dto);
        ctx.ui.success("Сцену оновлено.");
        return updated;
    }
}
