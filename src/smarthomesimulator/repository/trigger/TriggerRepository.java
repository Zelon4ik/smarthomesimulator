package smarthomesimulator.repository.trigger;

import com.google.gson.reflect.TypeToken;
import smarthomesimulator.Trigger.Trigger;
import smarthomesimulator.repository.AbstractRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TriggerRepository extends AbstractRepository<Trigger, String> {
    
    private static final String DATA_FILE = "triggers-data.json";
    private static final Type LIST_TYPE = new TypeToken<List<Trigger>>(){}.getType();
    
    public TriggerRepository() {
        super(DATA_FILE, LIST_TYPE);
    }
    
    @Override
    protected String getId(Trigger entity) {
        return entity.getId();
    }
    
    @Override
    protected boolean matchesSearch(Trigger entity, String query) {
        return entity.getCondition().toLowerCase().contains(query) ||
               entity.getAction().toLowerCase().contains(query) ||
               entity.getId().toLowerCase().contains(query);
    }
    
    @Override
    protected boolean matchesFilter(Trigger entity, String field, Object value) {
        switch (field.toLowerCase()) {
            case "condition":
                return entity.getCondition().equalsIgnoreCase(value.toString());
            case "action":
                return entity.getAction().equalsIgnoreCase(value.toString());
            default:
                return false;
        }
    }
}
