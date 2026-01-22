package smarthomesimulator.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import smarthomesimulator.cache.IdentityMap;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {
    
    protected final IdentityMap<ID, T> identityMap;
    protected final String dataFileName;
    protected final Gson gson;
    protected final Type listType;
    
    public AbstractRepository(String dataFileName, Type listType) {
        this.identityMap = new IdentityMap<>();
        this.dataFileName = dataFileName;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.listType = listType;
        loadFromFile();
    }
    
    protected abstract ID getId(T entity);
    
    @Override
    public void save(T entity) {
        ID id = getId(entity);
        identityMap.put(id, entity);
        saveToFile();
    }
    
    @Override
    public Optional<T> findById(ID id) {
        Optional<T> cached = identityMap.get(id);
        if (cached.isPresent()) {
            return cached;
        }
        // If not in cache, reload from file and try again
        loadFromFile();
        return identityMap.get(id);
    }
    
    @Override
    public List<T> findAll() {
        loadFromFile(); // Ensure we have latest data
        return new ArrayList<>(identityMap.values());
    }
    
    @Override
    public void update(T entity) {
        ID id = getId(entity);
        identityMap.put(id, entity);
        saveToFile();
    }
    
    @Override
    public void deleteById(ID id) {
        identityMap.remove(id);
        saveToFile();
    }
    
    @Override
    public List<T> search(String query) {
        List<T> all = findAll();
        if (query == null || query.trim().isEmpty()) {
            return all;
        }
        String lowerQuery = query.toLowerCase();
        return all.stream()
                .filter(entity -> matchesSearch(entity, lowerQuery))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<T> filter(Map<String, Object> criteria) {
        List<T> all = findAll();
        if (criteria == null || criteria.isEmpty()) {
            return all;
        }
        
        List<Predicate<T>> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            predicates.add(entity -> matchesFilter(entity, entry.getKey(), entry.getValue()));
        }
        
        return all.stream()
                .filter(predicates.stream().reduce(Predicate::and).orElse(x -> true))
                .collect(Collectors.toList());
    }
    
    protected abstract boolean matchesSearch(T entity, String query);
    
    protected abstract boolean matchesFilter(T entity, String field, Object value);
    
    protected void saveToFile() {
        try (FileWriter writer = new FileWriter(dataFileName)) {
            List<T> all = new ArrayList<>(identityMap.values());
            gson.toJson(all, writer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save data to file: " + dataFileName, e);
        }
    }
    
    protected void loadFromFile() {
        try (FileReader reader = new FileReader(dataFileName)) {
            List<T> entities = gson.fromJson(reader, listType);
            if (entities != null) {
                identityMap.clear();
                for (T entity : entities) {
                    identityMap.put(getId(entity), entity);
                }
            }
        } catch (Exception e) {
            // File doesn't exist yet or is empty, start with empty cache
            identityMap.clear();
        }
    }
}
