package smarthomesimulator.service;

import smarthomesimulator.Device.Device;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.Room.Room;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.User.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DocumentService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void exportDevicesToCsv(List<Device> devices, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,Name,Type,Is On,Power Consumption");
            writer.newLine();

            for (Device device : devices) {
                writer.write(String.format("%s,%s,%s,%s,%.2f",
                    escapeCsv(device.getId()),
                    escapeCsv(device.getName()),
                    escapeCsv(device.getType()),
                    device.isOn(),
                    device.getPowerConsumption()
                ));
                writer.newLine();
            }
        }
    }

    public void exportRoomsToCsv(List<Room> rooms, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,Name,Device Count,Device IDs");
            writer.newLine();

            for (Room room : rooms) {
                String deviceIds = room.getDevices().stream()
                    .map(Device::getId)
                    .reduce((a, b) -> a + ";" + b)
                    .orElse("");
                
                writer.write(String.format("%s,%s,%d,%s",
                    escapeCsv(room.getId()),
                    escapeCsv(room.getName()),
                    room.getDevices().size(),
                    escapeCsv(deviceIds)
                ));
                writer.newLine();
            }
        }
    }

    public void exportScenesToCsv(List<Scene> scenes, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,Name,Device Count,Device IDs");
            writer.newLine();

            for (Scene scene : scenes) {
                String deviceIds = scene.getDevices().stream()
                    .map(Device::getId)
                    .reduce((a, b) -> a + ";" + b)
                    .orElse("");
                
                writer.write(String.format("%s,%s,%d,%s",
                    escapeCsv(scene.getId()),
                    escapeCsv(scene.getName()),
                    scene.getDevices().size(),
                    escapeCsv(deviceIds)
                ));
                writer.newLine();
            }
        }
    }

    public void exportTriggersToCsv(List<Trigger> triggers, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,Condition,Action");
            writer.newLine();

            for (Trigger trigger : triggers) {
                writer.write(String.format("%s,%s,%s",
                    escapeCsv(trigger.getId()),
                    escapeCsv(trigger.getCondition()),
                    escapeCsv(trigger.getAction())
                ));
                writer.newLine();
            }
        }
    }

    public void exportEnergyLogsToCsv(List<EnergyLog> logs, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,Device ID,Energy Used,Timestamp,Date");
            writer.newLine();

            for (EnergyLog log : logs) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(log.getTimestamp()),
                    ZoneId.systemDefault()
                );
                
                writer.write(String.format("%s,%s,%.2f,%d,%s",
                    escapeCsv(log.getId()),
                    escapeCsv(log.getDeviceId()),
                    log.getEnergyUsed(),
                    log.getTimestamp(),
                    escapeCsv(dateTime.format(DATE_FORMATTER))
                ));
                writer.newLine();
            }
        }
    }

    public void exportUsersToCsv(List<User> users, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("ID,Name,Email,Room Count");
            writer.newLine();

            for (User user : users) {
                writer.write(String.format("%s,%s,%s,%d",
                    escapeCsv(user.getId()),
                    escapeCsv(user.getName()),
                    escapeCsv(user.getEmail()),
                    user.getRooms().size()
                ));
                writer.newLine();
            }
        }
    }

    public void exportAllToCsv(List<User> users, List<Device> devices, List<Room> rooms,
                               List<Scene> scenes, List<Trigger> triggers, List<EnergyLog> logs,
                               String directoryPath) throws IOException {
        Path dir = Paths.get(directoryPath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        exportUsersToCsv(users, dir.resolve("users.csv").toString());
        exportDevicesToCsv(devices, dir.resolve("devices.csv").toString());
        exportRoomsToCsv(rooms, dir.resolve("rooms.csv").toString());
        exportScenesToCsv(scenes, dir.resolve("scenes.csv").toString());
        exportTriggersToCsv(triggers, dir.resolve("triggers.csv").toString());
        exportEnergyLogsToCsv(logs, dir.resolve("energy_logs.csv").toString());
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public void createTextReport(List<User> users, List<Device> devices, List<Room> rooms,
                                 List<Scene> scenes, List<Trigger> triggers, List<EnergyLog> logs,
                                 String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("=== ЗВІТ СИМУЛЯТОРА РОЗУМНОГО ДОМУ ===");
            writer.newLine();
            writer.write("Створено: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.newLine();
            writer.newLine();

            writer.write("=== ПІДСУМОК ===");
            writer.newLine();
            writer.write("Користувачі: " + users.size());
            writer.newLine();
            writer.write("Пристрої: " + devices.size());
            writer.newLine();
            writer.write("Кімнати: " + rooms.size());
            writer.newLine();
            writer.write("Сцени: " + scenes.size());
            writer.newLine();
            writer.write("Тригери: " + triggers.size());
            writer.newLine();
            writer.write("Журнали енергії: " + logs.size());
            writer.newLine();
            writer.newLine();

            double totalEnergy = logs.stream()
                .mapToDouble(EnergyLog::getEnergyUsed)
                .sum();
            writer.write("Загальне споживання енергії: " + String.format("%.2f", totalEnergy) + " кВт·год");
            writer.newLine();
            writer.newLine();

            writer.write("=== ПРИСТРОЇ ===");
            writer.newLine();
            for (Device device : devices) {
                writer.write(String.format("- %s (%s): %s, Потужність: %.2f Вт",
                    device.getName(),
                    device.getType(),
                    device.isOn() ? "УВІМКНЕНО" : "ВИМКНЕНО",
                    device.getPowerConsumption()
                ));
                writer.newLine();
            }
        }
    }
}
