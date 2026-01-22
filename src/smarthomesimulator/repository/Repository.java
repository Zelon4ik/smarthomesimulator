package smarthomesimulator.repository;

import java.util.*;

public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void update(T entity);
    void deleteById(ID id);
    
    // Search methods
    List<T> search(String query);
    
    // Filter methods
    List<T> filter(Map<String, Object> criteria);
}
