package smarthomesimulator.service;

import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.dto.TriggerStoreDto;
import smarthomesimulator.dto.TriggerUpdateDto;
import smarthomesimulator.repository.trigger.TriggerRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TriggerService {
    private final TriggerRepository triggerRepository;

    public TriggerService(TriggerRepository triggerRepository) {
        this.triggerRepository = triggerRepository;
    }

    public Trigger createTrigger(TriggerStoreDto triggerDto) {
        Trigger trigger = new Trigger(
            triggerDto.getCondition(),
            triggerDto.getAction()
        );

        triggerRepository.save(trigger);
        return trigger;
    }

    public Trigger updateTrigger(String triggerId, TriggerUpdateDto triggerDto) {
        Trigger trigger = triggerRepository.findById(triggerId)
            .orElseThrow(() -> new IllegalArgumentException("Тригер не знайдено з id: " + triggerId));

        triggerDto.getCondition().ifPresent(trigger::setCondition);
        triggerDto.getAction().ifPresent(trigger::setAction);

        triggerRepository.update(trigger);
        return trigger;
    }

    public Optional<Trigger> findById(String triggerId) {
        return triggerRepository.findById(triggerId);
    }

    public List<Trigger> findAll() {
        return triggerRepository.findAll();
    }

    public void deleteTrigger(String triggerId) {
        if (!triggerRepository.findById(triggerId).isPresent()) {
            throw new IllegalArgumentException("Тригер не знайдено з id: " + triggerId);
        }
        triggerRepository.deleteById(triggerId);
    }

    public List<Trigger> searchTriggers(String query) {
        return triggerRepository.search(query);
    }

    public List<Trigger> filterTriggers(Map<String, Object> criteria) {
        return triggerRepository.filter(criteria);
    }
}
