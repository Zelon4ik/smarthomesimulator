package smarthomesimulator.service;

import smarthomesimulator.Device.Device;
import smarthomesimulator.dto.DeviceStoreDto;
import smarthomesimulator.dto.DeviceUpdateDto;
import smarthomesimulator.repository.device.DeviceRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device createDevice(DeviceStoreDto deviceDto) {
        Device device = new Device(
            UUID.randomUUID().toString(),
            deviceDto.getName(),
            deviceDto.getType(),
            deviceDto.isOn(),
            deviceDto.getPowerConsumption()
        );

        deviceRepository.save(device);
        return device;
    }

    public Device updateDevice(String deviceId, DeviceUpdateDto deviceDto) {
        Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId));

        deviceDto.getName().ifPresent(device::setName);
        deviceDto.getType().ifPresent(device::setType);
        deviceDto.getIsOn().ifPresent(device::setOn);
        deviceDto.getPowerConsumption().ifPresent(device::setPowerConsumption);

        deviceRepository.update(device);
        return device;
    }

    public Optional<Device> findById(String deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    public void deleteDevice(String deviceId) {
        if (!deviceRepository.findById(deviceId).isPresent()) {
            throw new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId);
        }
        deviceRepository.deleteById(deviceId);
    }

    public List<Device> searchDevices(String query) {
        return deviceRepository.search(query);
    }

    public List<Device> filterDevices(Map<String, Object> criteria) {
        return deviceRepository.filter(criteria);
    }

    public Device toggleDevice(String deviceId) {
        Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId));
        
        device.setOn(!device.isOn());
        deviceRepository.update(device);
        return device;
    }
}
