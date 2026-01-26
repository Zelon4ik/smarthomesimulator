package smarthomesimulator.service;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Room.Room;
import smarthomesimulator.dto.RoomStoreDto;
import smarthomesimulator.dto.RoomUpdateDto;
import smarthomesimulator.repository.device.DeviceRepository;
import smarthomesimulator.repository.room.RoomRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RoomService {
    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;

    public RoomService(RoomRepository roomRepository, DeviceRepository deviceRepository) {
        this.roomRepository = roomRepository;
        this.deviceRepository = deviceRepository;
    }

    public Room createRoom(RoomStoreDto roomDto) {
        List<Device> devices = roomDto.getDeviceIds().stream()
            .map(deviceId -> deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId)))
            .collect(Collectors.toList());

        Room room = new Room(
            UUID.randomUUID().toString(),
            roomDto.getName(),
            devices
        );

        roomRepository.save(room);
        return room;
    }

    public Room updateRoom(String roomId, RoomUpdateDto roomDto) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Кімнату не знайдено з id: " + roomId));

        roomDto.getName().ifPresent(room::setName);

        roomDto.getDeviceIds().ifPresent(deviceIds -> {
            List<Device> devices = deviceIds.stream()
                .map(deviceId -> deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId)))
                .collect(Collectors.toList());
            room.setDevices(devices);
        });

        roomRepository.update(room);
        return room;
    }

    public Optional<Room> findById(String roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public void deleteRoom(String roomId) {
        if (!roomRepository.findById(roomId).isPresent()) {
            throw new IllegalArgumentException("Кімнату не знайдено з id: " + roomId);
        }
        roomRepository.deleteById(roomId);
    }

    public List<Room> searchRooms(String query) {
        return roomRepository.search(query);
    }

    public List<Room> filterRooms(Map<String, Object> criteria) {
        return roomRepository.filter(criteria);
    }

    public Room addDeviceToRoom(String roomId, String deviceId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Кімнату не знайдено з id: " + roomId));
        
        Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId));

        List<Device> devices = room.getDevices();
        if (!devices.contains(device)) {
            devices.add(device);
            room.setDevices(devices);
            roomRepository.update(room);
        }

        return room;
    }

    public Room removeDeviceFromRoom(String roomId, String deviceId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Кімнату не знайдено з id: " + roomId));

        List<Device> devices = room.getDevices();
        devices.removeIf(d -> d.getId().equals(deviceId));
        room.setDevices(devices);
        roomRepository.update(room);

        return room;
    }
}
