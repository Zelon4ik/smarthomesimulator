package smarthomesimulator;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.EmailVerificationDto;
import smarthomesimulator.dto.LoginDto;
import smarthomesimulator.dto.UserStoreDto;
import smarthomesimulator.infrastructure.EmailService;
import smarthomesimulator.repository.user.UserRepository;
import smarthomesimulator.service.AuthenticationService;

/**
 * Автоматична демонстрація повної функціональності авторизації, реєстрації та підтвердження email.
 * 
 * Цей клас автоматично демонструє всі функції без інтерактивного введення:
 * - Реєстрацію нового користувача
 * - Відправку email з токеном підтвердження
 * - Спроби входу без підтвердження email
 * - Підтвердження email за токеном
 * - Успішну авторизацію після підтвердження
 * - Повторну відправку токену підтвердження
 * - Обробку невірних даних
 */
public class AuthenticationDemoAuto {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   Smart Home Simulator - Authentication & Registration   ║");
        System.out.println("║         Автоматична демонстрація функціональності         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Ініціалізація репозиторіїв та сервісів
        UserRepository userRepository = new UserRepository();
        EmailService emailService = EmailService.createDefault();
        AuthenticationService authService = new AuthenticationService(userRepository, emailService);

        try {
            // ========== ДЕМОНСТРАЦІЯ РЕЄСТРАЦІЇ ==========
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("1. РЕЄСТРАЦІЯ НОВОГО КОРИСТУВАЧА");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            String testEmail = "demo.user@example.com";
            String testName = "Demo User";
            String testPassword = "securePassword123";
            String verificationToken = null;
            User registeredUser;

            // Перевірка, чи користувач вже існує
            if (authService.userExists(testEmail)) {
                System.out.println("⚠️  Користувач з email " + testEmail + " вже існує.");
                registeredUser = authService.findByEmail(testEmail)
                    .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
                
                if (!registeredUser.isEmailVerified()) {
                    verificationToken = registeredUser.getEmailVerificationToken();
                    System.out.println("📧 Email не підтверджено. Токен доступний для підтвердження.");
                } else {
                    System.out.println("✅ Email вже підтверджено.");
                }
            } else {
                // Реєстрація нового користувача
                System.out.println("Реєстрація користувача:");
                System.out.println("  • Ім'я: " + testName);
                System.out.println("  • Email: " + testEmail);
                System.out.println("  • Пароль: " + testPassword + "\n");

                UserStoreDto registrationDto = new UserStoreDto(testName, testEmail, testPassword);
                registeredUser = authService.register(registrationDto);
                
                System.out.println("✅ Користувач успішно зареєстровано!");
                System.out.println("   ID: " + registeredUser.getId());
                System.out.println("   Ім'я: " + registeredUser.getName());
                System.out.println("   Email: " + registeredUser.getEmail());
                System.out.println("   Email підтверджено: " + registeredUser.isEmailVerified());
                
                // Отримуємо токен для демонстрації (в реальному застосунку він буде в email)
                verificationToken = registeredUser.getEmailVerificationToken();
                System.out.println("\n📧 Email з токеном підтвердження відправлено!");
                System.out.println("   Токен підтвердження: " + verificationToken);
            }

            // ========== ДЕМОНСТРАЦІЯ СПРОБИ ВХОДУ БЕЗ ПІДТВЕРДЖЕННЯ ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("2. СПРОБА ВХОДУ БЕЗ ПІДТВЕРДЖЕННЯ EMAIL");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            if (!registeredUser.isEmailVerified()) {
                System.out.println("Спроба входу з правильними даними...");
                try {
                    LoginDto loginDto = new LoginDto(testEmail, testPassword);
                    var loggedInUser = authService.login(loginDto);
                    loggedInUser.ifPresent(user -> 
                        System.out.println("✅ Вхід успішний: " + user.getName())
                    );
                } catch (IllegalStateException e) {
                    System.out.println("❌ Вхід заблоковано: " + e.getMessage());
                    System.out.println("   ℹ️  Необхідно спочатку підтвердити email!");
                }
            } else {
                System.out.println("✅ Email вже підтверджено, можна входити.");
            }

            // ========== ДЕМОНСТРАЦІЯ ПІДТВЕРДЖЕННЯ EMAIL ==========
            if (!registeredUser.isEmailVerified() && verificationToken != null) {
                System.out.println("\n═══════════════════════════════════════════════════════════");
                System.out.println("3. ПІДТВЕРДЖЕННЯ EMAIL");
                System.out.println("═══════════════════════════════════════════════════════════\n");

                System.out.println("Підтвердження email з токеном: " + verificationToken);
                
                try {
                    EmailVerificationDto verificationDto = new EmailVerificationDto(testEmail, verificationToken);
                    boolean verified = authService.verifyEmail(verificationDto);
                    
                    if (verified) {
                        System.out.println("\n✅ Email успішно підтверджено!");
                        System.out.println("📧 Підтвердження відправлено на email.");
                        
                        // Оновлюємо дані користувача
                        registeredUser = authService.findByEmail(testEmail)
                            .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
                        System.out.println("   Статус підтвердження: " + registeredUser.isEmailVerified());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("\n❌ Помилка підтвердження: " + e.getMessage());
                    
                    // Демонстрація повторної відправки токену
                    System.out.println("\n💡 Запит нового токену підтвердження...");
                    try {
                        authService.resendVerificationEmail(testEmail);
                        System.out.println("✅ Новий токен відправлено на email!");
                        registeredUser = authService.findByEmail(testEmail)
                            .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
                        System.out.println("   Новий токен: " + registeredUser.getEmailVerificationToken());
                    } catch (Exception ex) {
                        System.out.println("❌ Помилка: " + ex.getMessage());
                    }
                }
            }

            // ========== ДЕМОНСТРАЦІЯ УСПІШНОГО ВХОДУ ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("4. УСПІШНИЙ ВХІД ПІСЛЯ ПІДТВЕРДЖЕННЯ EMAIL");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            if (registeredUser.isEmailVerified()) {
                System.out.println("Спроба входу після підтвердження email...");
                try {
                    LoginDto loginDto = new LoginDto(testEmail, testPassword);
                    var loggedInUser = authService.login(loginDto);
                    
                    if (loggedInUser.isPresent()) {
                        User user = loggedInUser.get();
                        System.out.println("✅ ВХІД УСПІШНИЙ!");
                        System.out.println("   Ім'я: " + user.getName());
                        System.out.println("   Email: " + user.getEmail());
                        System.out.println("   Email підтверджено: " + user.isEmailVerified());
                        System.out.println("   ID користувача: " + user.getId());
                    } else {
                        System.out.println("❌ Невірний email або пароль.");
                    }
                } catch (IllegalStateException e) {
                    System.out.println("❌ " + e.getMessage());
                }
            } else {
                System.out.println("⚠️  Email не підтверджено. Вхід неможливий.");
            }

            // ========== ДЕМОНСТРАЦІЯ НЕВІРНОГО ПАРОЛЯ ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("5. СПРОБА ВХОДУ З НЕВІРНИМ ПАРОЛЕМ");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            System.out.println("Спроба входу з невірним паролем...");
            try {
                LoginDto wrongLoginDto = new LoginDto(testEmail, "wrongPassword123");
                var failedLogin = authService.login(wrongLoginDto);
                
                if (failedLogin.isEmpty()) {
                    System.out.println("❌ Вхід не вдався: невірний пароль.");
                }
            } catch (IllegalStateException e) {
                System.out.println("❌ " + e.getMessage());
            }

            // ========== ДЕМОНСТРАЦІЯ НЕВІРНОГО ТОКЕНУ ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("6. СПРОБА ПІДТВЕРДЖЕННЯ З НЕВІРНИМ ТОКЕНОМ");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            // Створюємо нового користувача для тестування
            String testEmail2 = "test.user2@example.com";
            if (!authService.userExists(testEmail2)) {
                UserStoreDto registrationDto2 = new UserStoreDto("Test User 2", testEmail2, "password123");
                User user2 = authService.register(registrationDto2);
                String wrongToken = "invalid_token_12345";
                
                System.out.println("Спроба підтвердження з невірним токеном...");
                try {
                    EmailVerificationDto wrongVerificationDto = new EmailVerificationDto(testEmail2, wrongToken);
                    authService.verifyEmail(wrongVerificationDto);
                    System.out.println("✅ Підтвердження успішне (неочікувано)");
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Помилка підтвердження: " + e.getMessage());
                    System.out.println("   ℹ️  Це очікувана поведінка - токен невірний.");
                }
            }

            // ========== ДЕМОНСТРАЦІЯ ПОВТОРНОЇ ВІДПРАВКИ ТОКЕНУ ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("7. ПОВТОРНА ВІДПРАВКА ТОКЕНУ ПІДТВЕРДЖЕННЯ");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            String testEmail3 = "resend.test@example.com";
            if (!authService.userExists(testEmail3)) {
                UserStoreDto registrationDto3 = new UserStoreDto("Resend Test User", testEmail3, "password123");
                User user3 = authService.register(registrationDto3);
                
                System.out.println("Запит нового токену підтвердження для: " + testEmail3);
                try {
                    authService.resendVerificationEmail(testEmail3);
                    System.out.println("✅ Новий токен відправлено на email!");
                    
                    User updatedUser = authService.findByEmail(testEmail3)
                        .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
                    System.out.println("   Новий токен: " + updatedUser.getEmailVerificationToken());
                } catch (Exception e) {
                    System.out.println("❌ Помилка: " + e.getMessage());
                }
            }

            // ========== ПІДСУМОК ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("ПІДСУМОК");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            User finalUser = authService.findByEmail(testEmail)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));

            System.out.println("📊 Інформація про основного користувача:");
            System.out.println("   • ID: " + finalUser.getId());
            System.out.println("   • Ім'я: " + finalUser.getName());
            System.out.println("   • Email: " + finalUser.getEmail());
            System.out.println("   • Email підтверджено: " + (finalUser.isEmailVerified() ? "✅ Так" : "❌ Ні"));
            System.out.println("   • Токен підтвердження: " + 
                (finalUser.getEmailVerificationToken() != null ? "Є (не підтверджено)" : "Відсутній (підтверджено)"));

            System.out.println("\n✅ Всі демонстрації завершено успішно!");
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("Функціональність, яка була продемонстрована:");
            System.out.println("  ✅ Реєстрація нового користувача");
            System.out.println("  ✅ Відправка email з токеном підтвердження");
            System.out.println("  ✅ Блокування входу без підтвердження email");
            System.out.println("  ✅ Підтвердження email за токеном");
            System.out.println("  ✅ Успішна авторизація після підтвердження");
            System.out.println("  ✅ Обробка невірних паролів");
            System.out.println("  ✅ Обробка невірних токенів");
            System.out.println("  ✅ Повторна відправка токену підтвердження");
            System.out.println("═══════════════════════════════════════════════════════════");

        } catch (Exception e) {
            System.out.println("\n❌ Помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
