package smarthomesimulator;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.Room.Room;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.User.User;
import smarthomesimulator.repository.Repository;
import smarthomesimulator.repository.device.DeviceRepository;
import smarthomesimulator.repository.energylog.EnergyLogRepository;
import smarthomesimulator.repository.room.RoomRepository;
import smarthomesimulator.repository.scene.SceneRepository;
import smarthomesimulator.repository.trigger.TriggerRepository;
import smarthomesimulator.repository.user.UserRepository;
import smarthomesimulator.unitofwork.UnitOfWork;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Smart Home Simulator - CRUD Operations Demo ===\n");

        // Initialize repositories
        UserRepository userRepo = new UserRepository();
        DeviceRepository deviceRepo = new DeviceRepository();
        RoomRepository roomRepo = new RoomRepository();
        SceneRepository sceneRepo = new SceneRepository();
        TriggerRepository triggerRepo = new TriggerRepository();
        EnergyLogRepository energyLogRepo = new EnergyLogRepository();

        // Initialize UnitOfWork
        UnitOfWork unitOfWork = new UnitOfWork(
                userRepo, deviceRepo, roomRepo, sceneRepo, triggerRepo, energyLogRepo
        );

        // ========== USER CRUD Operations ==========
        System.out.println("--- USER Operations ---");
        
        // CREATE
        User user1 = new User("Ivan Petrov", "ivan@mail.com", "hashed_password_1", new ArrayList<>());
        User user2 = new User("Maria Ivanova", "maria@mail.com", "hashed_password_2", new ArrayList<>());
        userRepo.save(user1);
        userRepo.save(user2);
        System.out.println("Created users: " + user1.getName() + ", " + user2.getName());

        // READ
        Optional<User> foundUser = userRepo.findById(user1.getId());
        foundUser.ifPresent(u -> System.out.println("Found user: " + u.getName() + " (" + u.getEmail() + ")"));
        
        List<User> allUsers = userRepo.findAll();
        System.out.println("Total users: " + allUsers.size());

        // UPDATE
        user1.setName("Ivan Petrov Updated");
        user1.setEmail("ivan.updated@mail.com");
        userRepo.update(user1);
        System.out.println("Updated user: " + user1.getName());

        // SEARCH
        List<User> searchResults = userRepo.search("Ivan");
        System.out.println("Search 'Ivan': " + searchResults.size() + " results");

        // FILTER
        Map<String, Object> filterCriteria = new HashMap<>();
        filterCriteria.put("email", "maria@mail.com");
        List<User> filteredUsers = userRepo.filter(filterCriteria);
        System.out.println("Filter by email 'maria@mail.com': " + filteredUsers.size() + " results");

        // ========== DEVICE CRUD Operations ==========
        System.out.println("\n--- DEVICE Operations ---");
        
        // CREATE
        Device device1 = new Device(UUID.randomUUID().toString(), "Smart Light", "Light", true, 50.5);
        Device device2 = new Device(UUID.randomUUID().toString(), "Smart Thermostat", "Thermostat", false, 25.0);
        Device device3 = new Device(UUID.randomUUID().toString(), "Smart TV", "TV", true, 120.0);
        deviceRepo.save(device1);
        deviceRepo.save(device2);
        deviceRepo.save(device3);
        System.out.println("Created devices: " + device1.getName() + ", " + device2.getName() + ", " + device3.getName());

        // READ
        Optional<Device> foundDevice = deviceRepo.findById(device1.getId());
        foundDevice.ifPresent(d -> System.out.println("Found device: " + d.getName() + " (" + d.getType() + ")"));

        // UPDATE
        device1.setName("Smart Light Updated");
        device1.setOn(false);
        deviceRepo.update(device1);
        System.out.println("Updated device: " + device1.getName() + " (isOn: " + device1.isOn() + ")");

        // SEARCH
        List<Device> deviceSearch = deviceRepo.search("Smart");
        System.out.println("Search 'Smart': " + deviceSearch.size() + " results");

        // FILTER
        Map<String, Object> deviceFilter = new HashMap<>();
        deviceFilter.put("type", "Light");
        List<Device> filteredDevices = deviceRepo.filter(deviceFilter);
        System.out.println("Filter by type 'Light': " + filteredDevices.size() + " results");

        deviceFilter.clear();
        deviceFilter.put("isOn", true);
        List<Device> onDevices = deviceRepo.filter(deviceFilter);
        System.out.println("Filter by isOn=true: " + onDevices.size() + " results");

        // ========== ROOM CRUD Operations ==========
        System.out.println("\n--- ROOM Operations ---");
        
        // CREATE
        Room room1 = new Room(UUID.randomUUID().toString(), "Living Room", Arrays.asList(device1, device3));
        Room room2 = new Room(UUID.randomUUID().toString(), "Bedroom", Arrays.asList(device2));
        roomRepo.save(room1);
        roomRepo.save(room2);
        System.out.println("Created rooms: " + room1.getName() + ", " + room2.getName());

        // READ
        Optional<Room> foundRoom = roomRepo.findById(room1.getId());
        foundRoom.ifPresent(r -> System.out.println("Found room: " + r.getName() + " (" + r.getDevices().size() + " devices)"));

        // UPDATE
        room1.setName("Living Room Updated");
        roomRepo.update(room1);
        System.out.println("Updated room: " + room1.getName());

        // SEARCH
        List<Room> roomSearch = roomRepo.search("Living");
        System.out.println("Search 'Living': " + roomSearch.size() + " results");

        // FILTER
        Map<String, Object> roomFilter = new HashMap<>();
        roomFilter.put("device_count", 2);
        List<Room> roomsWith2Devices = roomRepo.filter(roomFilter);
        System.out.println("Filter by device_count=2: " + roomsWith2Devices.size() + " results");

        // ========== SCENE CRUD Operations ==========
        System.out.println("\n--- SCENE Operations ---");
        
        // CREATE
        Scene scene1 = new Scene("Movie Night", Arrays.asList(device1, device3));
        Scene scene2 = new Scene("Sleep Mode", Arrays.asList(device2));
        sceneRepo.save(scene1);
        sceneRepo.save(scene2);
        System.out.println("Created scenes: " + scene1.getName() + ", " + scene2.getName());

        // READ
        Optional<Scene> foundScene = sceneRepo.findById(scene1.getId());
        foundScene.ifPresent(s -> System.out.println("Found scene: " + s.getName() + " (" + s.getDevices().size() + " devices)"));

        // UPDATE
        scene1.setName("Movie Night Updated");
        sceneRepo.update(scene1);
        System.out.println("Updated scene: " + scene1.getName());

        // SEARCH
        List<Scene> sceneSearch = sceneRepo.search("Movie");
        System.out.println("Search 'Movie': " + sceneSearch.size() + " results");

        // FILTER
        Map<String, Object> sceneFilter = new HashMap<>();
        sceneFilter.put("name", "Sleep Mode");
        List<Scene> filteredScenes = sceneRepo.filter(sceneFilter);
        System.out.println("Filter by name 'Sleep Mode': " + filteredScenes.size() + " results");

        // ========== TRIGGER CRUD Operations ==========
        System.out.println("\n--- TRIGGER Operations ---");
        
        // CREATE
        Trigger trigger1 = new Trigger("time > 22:00", "turn off all lights");
        Trigger trigger2 = new Trigger("temperature < 18", "turn on heating");
        triggerRepo.save(trigger1);
        triggerRepo.save(trigger2);
        System.out.println("Created triggers: " + trigger1.getCondition() + " -> " + trigger1.getAction());

        // READ
        Optional<Trigger> foundTrigger = triggerRepo.findById(trigger1.getId());
        foundTrigger.ifPresent(t -> System.out.println("Found trigger: " + t.getCondition() + " -> " + t.getAction()));

        // UPDATE
        trigger1.setAction("turn off all lights and set thermostat to 18");
        triggerRepo.update(trigger1);
        System.out.println("Updated trigger action: " + trigger1.getAction());

        // SEARCH
        List<Trigger> triggerSearch = triggerRepo.search("temperature");
        System.out.println("Search 'temperature': " + triggerSearch.size() + " results");

        // FILTER
        Map<String, Object> triggerFilter = new HashMap<>();
        triggerFilter.put("condition", "time > 22:00");
        List<Trigger> filteredTriggers = triggerRepo.filter(triggerFilter);
        System.out.println("Filter by condition 'time > 22:00': " + filteredTriggers.size() + " results");

        // ========== ENERGY LOG CRUD Operations ==========
        System.out.println("\n--- ENERGY LOG Operations ---");
        
        // CREATE
        long now = System.currentTimeMillis();
        EnergyLog log1 = new EnergyLog(device1.getId(), 150.5, now);
        EnergyLog log2 = new EnergyLog(device2.getId(), 75.0, now - 3600000); // 1 hour ago
        EnergyLog log3 = new EnergyLog(device3.getId(), 200.0, now);
        energyLogRepo.save(log1);
        energyLogRepo.save(log2);
        energyLogRepo.save(log3);
        System.out.println("Created energy logs: " + energyLogRepo.findAll().size() + " logs");

        // READ
        Optional<EnergyLog> foundLog = energyLogRepo.findById(log1.getId());
        foundLog.ifPresent(l -> System.out.println("Found log: device=" + l.getDeviceId() + ", energy=" + l.getEnergyUsed()));

        // UPDATE
        log1.setEnergyUsed(175.0);
        energyLogRepo.update(log1);
        System.out.println("Updated log energy: " + log1.getEnergyUsed());

        // SEARCH
        List<EnergyLog> logSearch = energyLogRepo.search(device1.getId());
        System.out.println("Search by device ID: " + logSearch.size() + " results");

        // FILTER
        Map<String, Object> logFilter = new HashMap<>();
        logFilter.put("device_id", device1.getId());
        List<EnergyLog> deviceLogs = energyLogRepo.filter(logFilter);
        System.out.println("Filter by device_id: " + deviceLogs.size() + " results");

        logFilter.clear();
        logFilter.put("energy_used", 150.5);
        List<EnergyLog> energyFiltered = energyLogRepo.filter(logFilter);
        System.out.println("Filter by energy_used=150.5: " + energyFiltered.size() + " results");

        // ========== UnitOfWork Commit ==========
        System.out.println("\n--- UnitOfWork Commit ---");
        unitOfWork.commit();
        System.out.println("All data committed to files");

        // ========== DELETE Operations ==========
        System.out.println("\n--- DELETE Operations ---");
        userRepo.deleteById(user2.getId());
        System.out.println("Deleted user: " + user2.getName());
        System.out.println("Remaining users: " + userRepo.findAll().size());

        deviceRepo.deleteById(device2.getId());
        System.out.println("Deleted device: " + device2.getName());
        System.out.println("Remaining devices: " + deviceRepo.findAll().size());

        System.out.println("\n=== Demo Complete ===");
    }
}
