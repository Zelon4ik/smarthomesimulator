package smarthomesimulator;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.Room.Room;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.User.User;
import smarthomesimulator.dto.*;
import smarthomesimulator.infrastructure.EmailService;
import smarthomesimulator.repository.device.DeviceRepository;
import smarthomesimulator.repository.energylog.EnergyLogRepository;
import smarthomesimulator.repository.room.RoomRepository;
import smarthomesimulator.repository.scene.SceneRepository;
import smarthomesimulator.repository.trigger.TriggerRepository;
import smarthomesimulator.repository.user.UserRepository;
import smarthomesimulator.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstration of the Domain Layer (Service Layer) with DTOs, Authentication, and Email functionality.
 */
public class ServiceLayerDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Smart Home Simulator - Domain Layer Demo ===\n");

        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        DeviceRepository deviceRepository = new DeviceRepository();
        RoomRepository roomRepository = new RoomRepository();
        SceneRepository sceneRepository = new SceneRepository();
        TriggerRepository triggerRepository = new TriggerRepository();
        EnergyLogRepository energyLogRepository = new EnergyLogRepository();

        // Initialize infrastructure services
        EmailService emailService = EmailService.createDefault();

        // Initialize domain services
        AuthenticationService authService = new AuthenticationService(userRepository, emailService);
        UserService userService = new UserService(userRepository);
        DeviceService deviceService = new DeviceService(deviceRepository);
        RoomService roomService = new RoomService(roomRepository, deviceRepository);
        SceneService sceneService = new SceneService(sceneRepository, deviceRepository);
        TriggerService triggerService = new TriggerService(triggerRepository);
        EnergyLogService energyLogService = new EnergyLogService(energyLogRepository, deviceRepository);
        DocumentService documentService = new DocumentService();

        // ========== AUTHENTICATION & REGISTRATION ==========
        System.out.println("--- Authentication & Registration ---");

        // Check if user already exists, if so use existing, otherwise register new
        User registeredUser;
        String testEmail = "john.doe@example.com";
        String verificationToken = null;
        
        if (authService.userExists(testEmail)) {
            System.out.println("User with email " + testEmail + " already exists, using existing user");
            registeredUser = authService.findByEmail(testEmail)
                .orElseThrow(() -> new RuntimeException("User should exist but was not found"));
            
            // If email not verified, get the token (in real app, this would come from email)
            if (!registeredUser.isEmailVerified() && registeredUser.getEmailVerificationToken() != null) {
                verificationToken = registeredUser.getEmailVerificationToken();
                System.out.println("Email not verified. Verification token available.");
            }
        } else {
            // Register a new user using DTO
            UserStoreDto registrationDto = new UserStoreDto(
                "John Doe",
                testEmail,
                "securePassword123"
            );
            registeredUser = authService.register(registrationDto);
            System.out.println("Registered user: " + registeredUser.getName() + " (" + registeredUser.getEmail() + ")");
            System.out.println("Email verification token has been sent to your email.");
            
            // Get the verification token (in real app, user would get it from email)
            verificationToken = registeredUser.getEmailVerificationToken();
            System.out.println("Verification token (for demo): " + verificationToken);
        }

        // Try to login before email verification (should fail)
        System.out.println("\n--- Attempting login before email verification ---");
        try {
            LoginDto loginDto = new LoginDto("john.doe@example.com", "securePassword123");
            var loggedInUser = authService.login(loginDto);
            loggedInUser.ifPresent(user -> System.out.println("Login successful: " + user.getName()));
        } catch (IllegalStateException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        // Verify email if token is available
        if (verificationToken != null && !registeredUser.isEmailVerified()) {
            System.out.println("\n--- Email Verification ---");
            try {
                EmailVerificationDto verificationDto = new EmailVerificationDto(testEmail, verificationToken);
                boolean verified = authService.verifyEmail(verificationDto);
                if (verified) {
                    System.out.println("Email verified successfully!");
                    // Refresh user data
                    registeredUser = authService.findByEmail(testEmail)
                        .orElseThrow(() -> new RuntimeException("User should exist"));
                }
            } catch (Exception e) {
                System.out.println("Verification failed: " + e.getMessage());
                // Try to resend verification email
                System.out.println("Resending verification email...");
                authService.resendVerificationEmail(testEmail);
                registeredUser = authService.findByEmail(testEmail)
                    .orElseThrow(() -> new RuntimeException("User should exist"));
                System.out.println("New verification token sent. Token: " + registeredUser.getEmailVerificationToken());
            }
        }

        // Try to login after email verification
        System.out.println("\n--- Attempting login after email verification ---");
        try {
            LoginDto loginDto = new LoginDto("john.doe@example.com", "securePassword123");
            var loggedInUser = authService.login(loginDto);
            loggedInUser.ifPresent(user -> {
                System.out.println("Login successful: " + user.getName());
                System.out.println("Email verified: " + user.isEmailVerified());
            });
        } catch (IllegalStateException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        // Try to login with wrong password
        System.out.println("\n--- Attempting login with wrong password ---");
        try {
            LoginDto wrongLoginDto = new LoginDto("john.doe@example.com", "wrongPassword");
            var failedLogin = authService.login(wrongLoginDto);
            if (failedLogin.isEmpty()) {
                System.out.println("Login failed (as expected with wrong password)");
            }
        } catch (IllegalStateException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        // ========== USER SERVICE ==========
        System.out.println("\n--- User Service ---");

        // Update user using DTO
        UserUpdateDto userUpdateDto = new UserUpdateDto(
            "John Doe Updated",
            null, // Email not changed
            null  // Password not changed
        );
        var updatedUser = userService.updateUser(registeredUser.getId(), userUpdateDto);
        System.out.println("Updated user name: " + updatedUser.getName());

        // Update password
        UserUpdateDto passwordUpdateDto = new UserUpdateDto(null, null, "newSecurePassword456");
        userService.updateUser(registeredUser.getId(), passwordUpdateDto);
        System.out.println("Password updated successfully");

        // ========== DEVICE SERVICE ==========
        System.out.println("\n--- Device Service ---");

        // Create device using DTO
        DeviceStoreDto deviceDto = new DeviceStoreDto(
            "Smart Light",
            "Light",
            true,
            50.5
        );
        var device = deviceService.createDevice(deviceDto);
        System.out.println("Created device: " + device.getName() + " (ID: " + device.getId() + ")");

        // Update device using DTO
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto(
            "Smart Light Pro",
            null, // Type not changed
            false, // Turn off
            null   // Power consumption not changed
        );
        var updatedDevice = deviceService.updateDevice(device.getId(), deviceUpdateDto);
        System.out.println("Updated device: " + updatedDevice.getName() + " (isOn: " + updatedDevice.isOn() + ")");

        // Toggle device
        deviceService.toggleDevice(device.getId());
        System.out.println("Device toggled. New state: " + deviceService.findById(device.getId()).get().isOn());

        // ========== ROOM SERVICE ==========
        System.out.println("\n--- Room Service ---");

        // Create another device for the room
        Device device2 = deviceService.createDevice(new DeviceStoreDto("Smart Thermostat", "Thermostat", false, 25.0));

        // Create room using DTO
        RoomStoreDto roomDto = new RoomStoreDto(
            "Living Room",
            List.of(device.getId(), device2.getId())
        );
        var room = roomService.createRoom(roomDto);
        System.out.println("Created room: " + room.getName() + " with " + room.getDevices().size() + " devices");

        // ========== SCENE SERVICE ==========
        System.out.println("\n--- Scene Service ---");

        // Create scene using DTO
        SceneStoreDto sceneDto = new SceneStoreDto(
            "Movie Night",
            List.of(device.getId(), device2.getId())
        );
        var scene = sceneService.createScene(sceneDto);
        System.out.println("Created scene: " + scene.getName());

        // Activate scene
        sceneService.activateScene(scene.getId());
        System.out.println("Scene activated - all devices turned on");

        // ========== TRIGGER SERVICE ==========
        System.out.println("\n--- Trigger Service ---");

        // Create trigger using DTO
        TriggerStoreDto triggerDto = new TriggerStoreDto(
            "time > 22:00",
            "turn off all lights"
        );
        var trigger = triggerService.createTrigger(triggerDto);
        System.out.println("Created trigger: " + trigger.getCondition() + " -> " + trigger.getAction());

        // ========== ENERGY LOG SERVICE ==========
        System.out.println("\n--- Energy Log Service ---");

        // Create energy log using DTO
        EnergyLogStoreDto logDto = new EnergyLogStoreDto(
            device.getId(),
            150.5,
            System.currentTimeMillis()
        );
        var log = energyLogService.createEnergyLog(logDto);
        System.out.println("Created energy log: " + log.getEnergyUsed() + " kWh for device " + log.getDeviceId());

        // Get total energy for device
        double totalEnergy = energyLogService.getTotalEnergyForDevice(device.getId());
        System.out.println("Total energy consumption for device: " + totalEnergy + " kWh");

        // ========== DOCUMENT SERVICE ==========
        System.out.println("\n--- Document Service ---");

        // Export all data to CSV
        List<User> allUsers = userService.findAll();
        List<Device> allDevices = deviceService.findAll();
        List<Room> allRooms = roomService.findAll();
        List<Scene> allScenes = sceneService.findAll();
        List<Trigger> allTriggers = triggerService.findAll();
        List<EnergyLog> allLogs = energyLogService.findAll();

        documentService.exportAllToCsv(
            allUsers,
            allDevices,
            allRooms,
            allScenes,
            allTriggers,
            allLogs,
            "exports"
        );
        System.out.println("Exported all data to CSV files in 'exports' directory");

        // Create text report
        documentService.createTextReport(
            allUsers,
            allDevices,
            allRooms,
            allScenes,
            allTriggers,
            allLogs,
            "report.txt"
        );
        System.out.println("Created text report: report.txt");

        // ========== SEARCH & FILTER ==========
        System.out.println("\n--- Search & Filter ---");

        // Search devices
        List<Device> searchResults = deviceService.searchDevices("Smart");
        System.out.println("Search 'Smart': " + searchResults.size() + " devices found");

        // Filter devices
        Map<String, Object> filterCriteria = new HashMap<>();
        filterCriteria.put("type", "Light");
        List<Device> filteredDevices = deviceService.filterDevices(filterCriteria);
        System.out.println("Filter by type 'Light': " + filteredDevices.size() + " devices found");

        System.out.println("\n=== Demo Complete ===");
    }
}
