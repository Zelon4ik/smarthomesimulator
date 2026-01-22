package smarthomesimulator.repository.energylog;

import com.google.gson.reflect.TypeToken;
import smarthomesimulator.EnergyLog.EnergyLog;
import smarthomesimulator.repository.AbstractRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class EnergyLogRepository extends AbstractRepository<EnergyLog, String> {
    
    private static final String DATA_FILE = "energy-logs-data.json";
    private static final Type LIST_TYPE = new TypeToken<List<EnergyLog>>(){}.getType();
    
    public EnergyLogRepository() {
        super(DATA_FILE, LIST_TYPE);
    }
    
    @Override
    protected String getId(EnergyLog entity) {
        return entity.getId();
    }
    
    @Override
    protected boolean matchesSearch(EnergyLog entity, String query) {
        return entity.getDeviceId().toLowerCase().contains(query) ||
               entity.getId().toLowerCase().contains(query) ||
               String.valueOf(entity.getEnergyUsed()).contains(query) ||
               String.valueOf(entity.getTimestamp()).contains(query);
    }
    
    @Override
    protected boolean matchesFilter(EnergyLog entity, String field, Object value) {
        switch (field.toLowerCase()) {
            case "deviceid":
            case "device_id":
                return entity.getDeviceId().equalsIgnoreCase(value.toString());
            case "energyused":
            case "energy_used":
            case "energy":
                if (value instanceof Number) {
                    return entity.getEnergyUsed() == ((Number) value).doubleValue();
                }
                try {
                    return entity.getEnergyUsed() == Double.parseDouble(value.toString());
                } catch (NumberFormatException e) {
                    return false;
                }
            case "timestamp":
            case "time":
                if (value instanceof Number) {
                    return entity.getTimestamp() == ((Number) value).longValue();
                }
                try {
                    return entity.getTimestamp() == Long.parseLong(value.toString());
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }
}
