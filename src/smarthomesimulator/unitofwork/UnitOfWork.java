package smarthomesimulator.unitofwork;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.Room.Room;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.User.User;
import smarthomesimulator.repository.Repository;

import java.io.FileWriter;
import java.util.List;

public class UnitOfWork {
    
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private final Repository<User, String> userRepository;
    private final Repository<Device, String> deviceRepository;
    private final Repository<Room, String> roomRepository;
    private final Repository<Scene, String> sceneRepository;
    private final Repository<Trigger, String> triggerRepository;
    private final Repository<EnergyLog, String> energyLogRepository;
    
    public UnitOfWork(
            Repository<User, String> userRepository,
            Repository<Device, String> deviceRepository,
            Repository<Room, String> roomRepository,
            Repository<Scene, String> sceneRepository,
            Repository<Trigger, String> triggerRepository,
            Repository<EnergyLog, String> energyLogRepository) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
        this.sceneRepository = sceneRepository;
        this.triggerRepository = triggerRepository;
        this.energyLogRepository = energyLogRepository;
    }
    
    public void commit() {
        // All repositories handle their own persistence through AbstractRepository
        // This method can be used for transactional operations if needed
        commitUsers();
        commitDevices();
        commitRooms();
        commitScenes();
        commitTriggers();
        commitEnergyLogs();
    }
    
    public void commitUsers() {
        List<User> users = userRepository.findAll();
        try (FileWriter writer = new FileWriter("smart-home-data.json")) {
            gson.toJson(users, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit users", e);
        }
    }
    
    public void commitDevices() {
        List<Device> devices = deviceRepository.findAll();
        try (FileWriter writer = new FileWriter("devices-data.json")) {
            gson.toJson(devices, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit devices", e);
        }
    }
    
    public void commitRooms() {
        List<Room> rooms = roomRepository.findAll();
        try (FileWriter writer = new FileWriter("rooms-data.json")) {
            gson.toJson(rooms, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit rooms", e);
        }
    }
    
    public void commitScenes() {
        List<Scene> scenes = sceneRepository.findAll();
        try (FileWriter writer = new FileWriter("scenes-data.json")) {
            gson.toJson(scenes, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit scenes", e);
        }
    }
    
    public void commitTriggers() {
        List<Trigger> triggers = triggerRepository.findAll();
        try (FileWriter writer = new FileWriter("triggers-data.json")) {
            gson.toJson(triggers, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit triggers", e);
        }
    }
    
    public void commitEnergyLogs() {
        List<EnergyLog> energyLogs = energyLogRepository.findAll();
        try (FileWriter writer = new FileWriter("energy-logs-data.json")) {
            gson.toJson(energyLogs, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit energy logs", e);
        }
    }
    
    // Convenience methods for accessing repositories
    public Repository<User, String> getUserRepository() {
        return userRepository;
    }
    
    public Repository<Device, String> getDeviceRepository() {
        return deviceRepository;
    }
    
    public Repository<Room, String> getRoomRepository() {
        return roomRepository;
    }
    
    public Repository<Scene, String> getSceneRepository() {
        return sceneRepository;
    }
    
    public Repository<Trigger, String> getTriggerRepository() {
        return triggerRepository;
    }
    
    public Repository<EnergyLog, String> getEnergyLogRepository() {
        return energyLogRepository;
    }
}
