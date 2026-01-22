package smarthomesimulator.repository.user;

import smarthomesimulator.User.User;
import smarthomesimulator.repository.Repository;

import java.util.*;

public class UserRepository implements Repository<User, String> {

    private final Map<String, User> cache = new HashMap<>();

    @Override
    public void save(User user) {
        cache.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public void update(User user) {
        cache.put(user.getId(), user);
    }

    @Override
    public void deleteById(String id) {
        cache.remove(id);
    }
}
