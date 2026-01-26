package smarthomesimulator.service;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.UserStoreDto;
import smarthomesimulator.dto.UserUpdateDto;
import smarthomesimulator.infrastructure.PasswordHasher;
import smarthomesimulator.repository.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserStoreDto userDto) {
        List<User> existingUsers = userRepository.findAll();
        boolean emailExists = existingUsers.stream()
            .anyMatch(user -> user.getEmail().equalsIgnoreCase(userDto.getEmail()));

        if (emailExists) {
            throw new IllegalArgumentException("Користувач з email " + userDto.getEmail() + " вже існує");
        }

        String passwordHash = PasswordHasher.hashPassword(userDto.getPassword());

        User newUser = new User(
            userDto.getName(),
            userDto.getEmail(),
            passwordHash,
            List.of()
        );

        userRepository.save(newUser);
        return newUser;
    }

    public User updateUser(String userId, UserUpdateDto userDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Користувача не знайдено з id: " + userId));

        userDto.getName().ifPresent(user::setName);

        userDto.getEmail().ifPresent(email -> {
            List<User> existingUsers = userRepository.findAll();
            boolean emailExists = existingUsers.stream()
                .anyMatch(u -> !u.getId().equals(userId) && u.getEmail().equalsIgnoreCase(email));
            
            if (emailExists) {
                throw new IllegalArgumentException("Email " + email + " вже зайнятий");
            }
            user.setEmail(email);
        });

        userDto.getPassword().ifPresent(password -> {
            String passwordHash = PasswordHasher.hashPassword(password);
            user.setPasswordHash(passwordHash);
        });

        userRepository.update(user);
        return user;
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUser(String userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("Користувача не знайдено з id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    public List<User> searchUsers(String query) {
        return userRepository.search(query);
    }

    public List<User> filterUsers(Map<String, Object> criteria) {
        return userRepository.filter(criteria);
    }
}
