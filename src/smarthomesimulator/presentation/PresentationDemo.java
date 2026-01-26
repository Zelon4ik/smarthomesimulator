package smarthomesimulator.presentation;

import smarthomesimulator.infrastructure.EmailService;
import smarthomesimulator.presentation.pages.AuthView;
import smarthomesimulator.repository.device.DeviceRepository;
import smarthomesimulator.repository.energylog.EnergyLogRepository;
import smarthomesimulator.repository.room.RoomRepository;
import smarthomesimulator.repository.scene.SceneRepository;
import smarthomesimulator.repository.trigger.TriggerRepository;
import smarthomesimulator.repository.user.UserRepository;

import java.util.Scanner;

public class PresentationDemo {
    public static void main(String[] args) throws Exception {
        Console.title("Симулятор Розумного Дому - Шар Представлення");
        Console.blankLine();

        UserRepository userRepository = new UserRepository();
        DeviceRepository deviceRepository = new DeviceRepository();
        RoomRepository roomRepository = new RoomRepository();
        SceneRepository sceneRepository = new SceneRepository();
        TriggerRepository triggerRepository = new TriggerRepository();
        EnergyLogRepository energyLogRepository = new EnergyLogRepository();

        EmailService emailService = EmailService.createDefault();

        try (Scanner scanner = new Scanner(System.in)) {
            AppContext ctx = new AppContext(
                scanner,
                userRepository,
                deviceRepository,
                roomRepository,
                sceneRepository,
                triggerRepository,
                energyLogRepository,
                emailService
            );

            View current = new AuthView();
            while (current != null) {
                try {
                    current = current.render(ctx);
                } catch (SecurityException se) {
                    Console.error(se.getMessage());
                } catch (IllegalArgumentException iae) {
                    Console.error("Помилка: " + iae.getMessage());
                }
            }
        }

        Console.blankLine();
        Console.success("До побачення.");
    }
}
