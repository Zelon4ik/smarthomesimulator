package smarthomesimulator.repository.device;

import com.google.gson.reflect.TypeToken;
import smarthomesimulator.Device.Device;
import smarthomesimulator.repository.AbstractRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class DeviceRepository extends AbstractRepository<Device, String> {
    
    private static final String DATA_FILE = "devices-data.json";
    private static final Type LIST_TYPE = new TypeToken<List<Device>>(){}.getType();
    
    public DeviceRepository() {
        super(DATA_FILE, LIST_TYPE);
    }
    
    @Override
    protected String getId(Device entity) {
        return entity.getId();
    }
    
    @Override
    protected boolean matchesSearch(Device entity, String query) {
        return entity.getName().toLowerCase().contains(query) ||
               entity.getType().toLowerCase().contains(query) ||
               entity.getId().toLowerCase().contains(query);
    }
    
    @Override
    protected boolean matchesFilter(Device entity, String field, Object value) {
        switch (field.toLowerCase()) {
            case "name":
                return entity.getName().equalsIgnoreCase(value.toString());
            case "type":
                return entity.getType().equalsIgnoreCase(value.toString());
            case "ison":
            case "is_on":
            case "on":
                if (value instanceof Boolean) {
                    return entity.isOn() == (Boolean) value;
                }
                return String.valueOf(entity.isOn()).equalsIgnoreCase(value.toString());
            case "powerconsumption":
            case "power_consumption":
            case "power":
                if (value instanceof Number) {
                    return entity.getPowerConsumption() == ((Number) value).doubleValue();
                }
                try {
                    return entity.getPowerConsumption() == Double.parseDouble(value.toString());
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }
}
