package smarthomesimulator.presentation;

import smarthomesimulator.infrastructure.EmailService;
import smarthomesimulator.presentation.consoleui.ConsoleUI;
import smarthomesimulator.repository.device.DeviceRepository;
import smarthomesimulator.repository.energylog.EnergyLogRepository;
import smarthomesimulator.repository.room.RoomRepository;
import smarthomesimulator.repository.scene.SceneRepository;
import smarthomesimulator.repository.trigger.TriggerRepository;
import smarthomesimulator.repository.user.UserRepository;
import smarthomesimulator.service.*;

import java.util.Objects;
import java.util.Scanner;

public final class AppContext {
    public final Scanner scanner;
    public final ConsoleUI ui;

    public final UserRepository userRepository;
    public final DeviceRepository deviceRepository;
    public final RoomRepository roomRepository;
    public final SceneRepository sceneRepository;
    public final TriggerRepository triggerRepository;
    public final EnergyLogRepository energyLogRepository;

    public final EmailService emailService;

    public final AuthenticationService authenticationService;
    public final UserService userService;
    public final DeviceService deviceService;
    public final RoomService roomService;
    public final SceneService sceneService;
    public final TriggerService triggerService;
    public final EnergyLogService energyLogService;
    public final DocumentService documentService;

    public final Session session;

    public AppContext(
        Scanner scanner,
        UserRepository userRepository,
        DeviceRepository deviceRepository,
        RoomRepository roomRepository,
        SceneRepository sceneRepository,
        TriggerRepository triggerRepository,
        EnergyLogRepository energyLogRepository,
        EmailService emailService
    ) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
        this.ui = new ConsoleUI(this.scanner);

        this.userRepository = Objects.requireNonNull(userRepository, "userRepository");
        this.deviceRepository = Objects.requireNonNull(deviceRepository, "deviceRepository");
        this.roomRepository = Objects.requireNonNull(roomRepository, "roomRepository");
        this.sceneRepository = Objects.requireNonNull(sceneRepository, "sceneRepository");
        this.triggerRepository = Objects.requireNonNull(triggerRepository, "triggerRepository");
        this.energyLogRepository = Objects.requireNonNull(energyLogRepository, "energyLogRepository");

        this.emailService = Objects.requireNonNull(emailService, "emailService");

        this.authenticationService = new AuthenticationService(userRepository, emailService);
        this.userService = new UserService(userRepository);
        this.deviceService = new DeviceService(deviceRepository);
        this.roomService = new RoomService(roomRepository, deviceRepository);
        this.sceneService = new SceneService(sceneRepository, deviceRepository);
        this.triggerService = new TriggerService(triggerRepository);
        this.energyLogService = new EnergyLogService(energyLogRepository, deviceRepository);
        this.documentService = new DocumentService();

        this.session = new Session();
    }

    public Role inferRoleFromEmail(String email) {
        if (email == null) {
            return Role.VIEWER;
        }
        String e = email.trim().toLowerCase();
        if (e.startsWith("admin") || e.endsWith("@admin.com")) {
            return Role.ADMIN;
        }
        if (e.endsWith("@operator.com")) {
            return Role.OPERATOR;
        }
        return Role.VIEWER;
    }
}
