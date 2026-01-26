package smarthomesimulator.presentation.pages;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.Room.Room;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.User.User;
import smarthomesimulator.presentation.AccessControl;
import smarthomesimulator.presentation.AppContext;
import smarthomesimulator.presentation.Permission;
import smarthomesimulator.presentation.View;

import java.util.List;

public final class DocumentsView implements View {
    @Override
    public View render(AppContext ctx) throws Exception {
        AccessControl.require(ctx.session, Permission.DOCUMENTS_EXPORT, "Експорт документів");

        ctx.ui.hr();
        ctx.ui.headline("Документи / Експорт");

        List<String> menu = List.of(
            "Експортувати все в CSV",
            "Створити текстовий звіт",
            "Назад"
        );

        int choice = ctx.ui.select("Дії", menu);
        switch (choice) {
            case 0: {
                String dir = ctx.ui.promptOptional("Вихідна директорія (за замовчуванням: exports)");
                if (dir.trim().isEmpty()) dir = "exports";

                List<User> users = ctx.userService.findAll();
                List<Device> devices = ctx.deviceService.findAll();
                List<Room> rooms = ctx.roomService.findAll();
                List<Scene> scenes = ctx.sceneService.findAll();
                List<Trigger> triggers = ctx.triggerService.findAll();
                List<EnergyLog> logs = ctx.energyLogService.findAll();

                ctx.documentService.exportAllToCsv(users, devices, rooms, scenes, triggers, logs, dir);
                ctx.ui.success("CSV файли експортовано до: " + dir);
                return this;
            }
            case 1: {
                String path = ctx.ui.promptOptional("Шлях до файлу звіту (за замовчуванням: report.txt)");
                if (path.trim().isEmpty()) path = "report.txt";

                List<User> users = ctx.userService.findAll();
                List<Device> devices = ctx.deviceService.findAll();
                List<Room> rooms = ctx.roomService.findAll();
                List<Scene> scenes = ctx.sceneService.findAll();
                List<Trigger> triggers = ctx.triggerService.findAll();
                List<EnergyLog> logs = ctx.energyLogService.findAll();

                ctx.documentService.createTextReport(users, devices, rooms, scenes, triggers, logs, path);
                ctx.ui.success("Звіт створено: " + path);
                return this;
            }
            default:
                return new MainMenuView();
        }
    }
}
