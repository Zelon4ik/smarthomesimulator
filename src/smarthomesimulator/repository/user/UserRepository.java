package smarthomesimulator.repository.user;

import com.google.gson.reflect.TypeToken;
import smarthomesimulator.User.User;
import smarthomesimulator.repository.AbstractRepository;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class UserRepository extends AbstractRepository<User, String> {
    
    private static final String DATA_FILE = "smart-home-data.json";
    private static final Type LIST_TYPE = new TypeToken<List<User>>(){}.getType();
    
    public UserRepository() {
        super(DATA_FILE, LIST_TYPE);
    }
    
    @Override
    protected String getId(User entity) {
        return entity.getId();
    }
    
    @Override
    protected boolean matchesSearch(User entity, String query) {
        return entity.getName().toLowerCase().contains(query) ||
               entity.getEmail().toLowerCase().contains(query) ||
               entity.getId().toLowerCase().contains(query) ||
               entity.getRooms().stream()
                   .anyMatch(room -> room.getName().toLowerCase().contains(query));
    }
    
    @Override
    protected boolean matchesFilter(User entity, String field, Object value) {
        switch (field.toLowerCase()) {
            case "name":
                return entity.getName().equalsIgnoreCase(value.toString());
            case "email":
                return entity.getEmail().equalsIgnoreCase(value.toString());
            case "roomcount":
            case "room_count":
            case "rooms":
                if (value instanceof Number) {
                    return entity.getRooms().size() == ((Number) value).intValue();
                }
                try {
                    return entity.getRooms().size() == Integer.parseInt(value.toString());
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }
}
