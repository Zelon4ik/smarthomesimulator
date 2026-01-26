package smarthomesimulator.service;

import smarthomesimulator.Device.Device;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.dto.SceneStoreDto;
import smarthomesimulator.dto.SceneUpdateDto;
import smarthomesimulator.repository.device.DeviceRepository;
import smarthomesimulator.repository.scene.SceneRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SceneService {
    private final SceneRepository sceneRepository;
    private final DeviceRepository deviceRepository;

    public SceneService(SceneRepository sceneRepository, DeviceRepository deviceRepository) {
        this.sceneRepository = sceneRepository;
        this.deviceRepository = deviceRepository;
    }

    public Scene createScene(SceneStoreDto sceneDto) {
        List<Device> devices = sceneDto.getDeviceIds().stream()
            .map(deviceId -> deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId)))
            .collect(Collectors.toList());

        Scene scene = new Scene(
            sceneDto.getName(),
            devices
        );

        sceneRepository.save(scene);
        return scene;
    }

    public Scene updateScene(String sceneId, SceneUpdateDto sceneDto) {
        Scene scene = sceneRepository.findById(sceneId)
            .orElseThrow(() -> new IllegalArgumentException("Сцену не знайдено з id: " + sceneId));

        sceneDto.getName().ifPresent(scene::setName);

        sceneDto.getDeviceIds().ifPresent(deviceIds -> {
            List<Device> devices = deviceIds.stream()
                .map(deviceId -> deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId)))
                .collect(Collectors.toList());
            scene.setDevices(devices);
        });

        sceneRepository.update(scene);
        return scene;
    }

    public Optional<Scene> findById(String sceneId) {
        return sceneRepository.findById(sceneId);
    }

    public List<Scene> findAll() {
        return sceneRepository.findAll();
    }

    public void deleteScene(String sceneId) {
        if (!sceneRepository.findById(sceneId).isPresent()) {
            throw new IllegalArgumentException("Сцену не знайдено з id: " + sceneId);
        }
        sceneRepository.deleteById(sceneId);
    }

    public List<Scene> searchScenes(String query) {
        return sceneRepository.search(query);
    }

    public List<Scene> filterScenes(Map<String, Object> criteria) {
        return sceneRepository.filter(criteria);
    }

    public Scene activateScene(String sceneId) {
        Scene scene = sceneRepository.findById(sceneId)
            .orElseThrow(() -> new IllegalArgumentException("Сцену не знайдено з id: " + sceneId));

        scene.getDevices().forEach(device -> {
            device.setOn(true);
            deviceRepository.update(device);
        });

        return scene;
    }

    public Scene deactivateScene(String sceneId) {
        Scene scene = sceneRepository.findById(sceneId)
            .orElseThrow(() -> new IllegalArgumentException("Сцену не знайдено з id: " + sceneId));

        scene.getDevices().forEach(device -> {
            device.setOn(false);
            deviceRepository.update(device);
        });

        return scene;
    }
}
