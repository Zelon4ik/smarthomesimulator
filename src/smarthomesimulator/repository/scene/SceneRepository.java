package smarthomesimulator.repository.scene;

import com.google.gson.reflect.TypeToken;
import smarthomesimulator.Scene.Scene;
import smarthomesimulator.repository.AbstractRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class SceneRepository extends AbstractRepository<Scene, String> {
    
    private static final String DATA_FILE = "scenes-data.json";
    private static final Type LIST_TYPE = new TypeToken<List<Scene>>(){}.getType();
    
    public SceneRepository() {
        super(DATA_FILE, LIST_TYPE);
    }
    
    @Override
    protected String getId(Scene entity) {
        return entity.getId();
    }
    
    @Override
    protected boolean matchesSearch(Scene entity, String query) {
        return entity.getName().toLowerCase().contains(query) ||
               entity.getId().toLowerCase().contains(query) ||
               entity.getDevices().stream()
                   .anyMatch(device -> device.getName().toLowerCase().contains(query) ||
                                     device.getType().toLowerCase().contains(query));
    }
    
    @Override
    protected boolean matchesFilter(Scene entity, String field, Object value) {
        switch (field.toLowerCase()) {
            case "name":
                return entity.getName().equalsIgnoreCase(value.toString());
            case "devicecount":
            case "device_count":
            case "devices":
                if (value instanceof Number) {
                    return entity.getDevices().size() == ((Number) value).intValue();
                }
                try {
                    return entity.getDevices().size() == Integer.parseInt(value.toString());
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }
}
