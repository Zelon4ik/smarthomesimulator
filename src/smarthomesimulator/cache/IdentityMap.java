package smarthomesimulator.cache;

import java.util.*;

public class IdentityMap<ID, T> {

    private final Map<ID, T> map = new HashMap<>();

    public void put(ID id, T entity) {
        map.put(id, entity);
    }

    public Optional<T> get(ID id) {
        return Optional.ofNullable(map.get(id));
    }

    public Collection<T> values() {
        return map.values();
    }

    public void remove(ID id) {
        map.remove(id);
    }
    
    public void clear() {
        map.clear();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
}
