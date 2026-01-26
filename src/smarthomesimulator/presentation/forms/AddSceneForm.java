package smarthomesimulator.presentation.forms;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.dto.SceneStoreDto;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;

import java.util.List;
import java.util.stream.Collectors;

public final class AddSceneForm {
    public Scene run(AppContext ctx) {
        AccessControl.require(ctx.session, Permission.SCENES_CREATE, "Додати сцену");

        ctx.ui.hr();
        ctx.ui.headline("Додати сцену");

        String name = ctx.ui.prompt("Назва", true, String::trim, v -> v.trim().isEmpty() ? "Назва обов'язкова" : null);

        List<Device> devices = ctx.deviceService.findAll();
        List<String> labels = devices.stream()
            .map(d -> d.getName() + " (" + d.getType() + ")")
            .collect(Collectors.toList());
        List<Integer> selected = ctx.ui.multiSelect("Виберіть пристрої для сцени", labels);
        List<String> deviceIds = selected.stream().map(i -> devices.get(i).getId()).collect(Collectors.toList());

        Scene scene = ctx.sceneService.createScene(new SceneStoreDto(name, deviceIds));
        ctx.ui.success("Сцену створено.");
        return scene;
    }
}
