package smarthomesimulator.service;

import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.dto.EnergyLogStoreDto;
import smarthomesimulator.dto.EnergyLogUpdateDto;
import smarthomesimulator.repository.device.DeviceRepository;
import smarthomesimulator.repository.energylog.EnergyLogRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EnergyLogService {
    private final EnergyLogRepository energyLogRepository;
    private final DeviceRepository deviceRepository;

    public EnergyLogService(EnergyLogRepository energyLogRepository, DeviceRepository deviceRepository) {
        this.energyLogRepository = energyLogRepository;
        this.deviceRepository = deviceRepository;
    }

    public EnergyLog createEnergyLog(EnergyLogStoreDto logDto) {
        if (!deviceRepository.findById(logDto.getDeviceId()).isPresent()) {
            throw new IllegalArgumentException("Пристрій не знайдено з id: " + logDto.getDeviceId());
        }

        EnergyLog log = new EnergyLog(
            logDto.getDeviceId(),
            logDto.getEnergyUsed(),
            logDto.getTimestamp()
        );

        energyLogRepository.save(log);
        return log;
    }

    public EnergyLog updateEnergyLog(String logId, EnergyLogUpdateDto logDto) {
        EnergyLog log = energyLogRepository.findById(logId)
            .orElseThrow(() -> new IllegalArgumentException("Журнал енергії не знайдено з id: " + logId));

        logDto.getDeviceId().ifPresent(deviceId -> {
            if (!deviceRepository.findById(deviceId).isPresent()) {
                throw new IllegalArgumentException("Пристрій не знайдено з id: " + deviceId);
            }
            log.setDeviceId(deviceId);
        });

        logDto.getEnergyUsed().ifPresent(log::setEnergyUsed);
        logDto.getTimestamp().ifPresent(log::setTimestamp);

        energyLogRepository.update(log);
        return log;
    }

    public Optional<EnergyLog> findById(String logId) {
        return energyLogRepository.findById(logId);
    }

    public List<EnergyLog> findAll() {
        return energyLogRepository.findAll();
    }

    public void deleteEnergyLog(String logId) {
        if (!energyLogRepository.findById(logId).isPresent()) {
            throw new IllegalArgumentException("Журнал енергії не знайдено з id: " + logId);
        }
        energyLogRepository.deleteById(logId);
    }

    public List<EnergyLog> searchEnergyLogs(String query) {
        return energyLogRepository.search(query);
    }

    public List<EnergyLog> filterEnergyLogs(Map<String, Object> criteria) {
        return energyLogRepository.filter(criteria);
    }

    public List<EnergyLog> getLogsByDevice(String deviceId) {
        Map<String, Object> criteria = Map.of("device_id", deviceId);
        return energyLogRepository.filter(criteria);
    }

    public double getTotalEnergyForDevice(String deviceId) {
        List<EnergyLog> logs = getLogsByDevice(deviceId);
        return logs.stream()
            .mapToDouble(EnergyLog::getEnergyUsed)
            .sum();
    }
}
