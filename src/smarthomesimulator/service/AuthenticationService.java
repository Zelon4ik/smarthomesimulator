package smarthomesimulator.service;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.EmailVerificationDto;
import smarthomesimulator.dto.LoginDto;
import smarthomesimulator.dto.UserStoreDto;
import smarthomesimulator.infrastructure.EmailService;
import smarthomesimulator.infrastructure.PasswordHasher;
import smarthomesimulator.infrastructure.TokenGenerator;
import smarthomesimulator.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

public class AuthenticationService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User register(UserStoreDto userDto) {
        List<User> existingUsers = userRepository.findAll();
        boolean emailExists = existingUsers.stream()
            .anyMatch(user -> user.getEmail().equalsIgnoreCase(userDto.getEmail()));

        if (emailExists) {
            throw new IllegalArgumentException("Користувач з email " + userDto.getEmail() + " вже існує");
        }

        String passwordHash = PasswordHasher.hashPassword(userDto.getPassword());
        String verificationToken = TokenGenerator.generateEmailVerificationToken();

        User newUser = new User(
            userDto.getName(),
            userDto.getEmail(),
            passwordHash,
            List.of()
        );
        
        newUser.setEmailVerificationToken(verificationToken);
        newUser.setEmailVerified(false);

        userRepository.save(newUser);
        emailService.sendRegistrationEmail(newUser.getEmail(), newUser.getName(), verificationToken);

        return newUser;
    }

    public Optional<User> login(LoginDto loginDto) {
        List<User> users = userRepository.findAll();
        
        Optional<User> user = users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(loginDto.getEmail()))
            .filter(u -> PasswordHasher.verifyPassword(loginDto.getPassword(), u.getPasswordHash()))
            .findFirst();
        
        if (user.isPresent() && !user.get().isEmailVerified()) {
            throw new IllegalStateException("Email не підтверджено. Будь ласка, підтвердіть ваш email перед входом.");
        }
        
        return user;
    }

    public boolean verifyEmail(EmailVerificationDto verificationDto) {
        Optional<User> userOpt = findByEmail(verificationDto.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Користувача з email " + verificationDto.getEmail() + " не знайдено");
        }
        
        User user = userOpt.get();
        
        if (user.isEmailVerified()) {
            return true;
        }
        
        if (user.getEmailVerificationToken() == null || 
            !user.getEmailVerificationToken().equals(verificationDto.getToken())) {
            throw new IllegalArgumentException("Невірний токен підтвердження");
        }
        
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.update(user);
        emailService.sendEmailVerificationConfirmation(user.getEmail(), user.getName());
        
        return true;
    }

    public void resendVerificationEmail(String email) {
        Optional<User> userOpt = findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Користувача з email " + email + " не знайдено");
        }
        
        User user = userOpt.get();
        
        if (user.isEmailVerified()) {
            throw new IllegalStateException("Email вже підтверджено");
        }
        
        String newToken = TokenGenerator.generateEmailVerificationToken();
        user.setEmailVerificationToken(newToken);
        userRepository.update(user);
        emailService.sendRegistrationEmail(user.getEmail(), user.getName(), newToken);
    }

    public boolean userExists(String email) {
        List<User> users = userRepository.findAll();
        return users.stream()
            .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = userRepository.findAll();
        return users.stream()
            .filter(user -> user.getEmail().equalsIgnoreCase(email))
            .findFirst();
    }
}
