package smarthomesimulator;

import smarthomesimulator.User.User;
import smarthomesimulator.dto.EmailVerificationDto;
import smarthomesimulator.dto.LoginDto;
import smarthomesimulator.dto.UserStoreDto;
import smarthomesimulator.infrastructure.EmailService;
import smarthomesimulator.repository.user.UserRepository;
import smarthomesimulator.service.AuthenticationService;

import java.util.Scanner;

/**
 * Демонстрація повної функціональності авторизації, реєстрації та підтвердження email.
 * 
 * Цей клас демонструє:
 * - Реєстрацію нового користувача
 * - Відправку email з токеном підтвердження
 * - Підтвердження email за токеном
 * - Авторизацію користувача
 * - Повторну відправку токену підтвердження
 */
public class AuthenticationDemo {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   Smart Home Simulator - Authentication & Registration   ║");
        System.out.println("║              Демонстрація повної функціональності        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Ініціалізація репозиторіїв та сервісів
        UserRepository userRepository = new UserRepository();
        EmailService emailService = EmailService.createDefault();
        AuthenticationService authService = new AuthenticationService(userRepository, emailService);

        Scanner scanner = new Scanner(System.in);

        try {
            // ========== ДЕМОНСТРАЦІЯ РЕЄСТРАЦІЇ ==========
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.println("1. РЕЄСТРАЦІЯ НОВОГО КОРИСТУВАЧА");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            System.out.print("Введіть ім'я: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                name = "Test User";
                System.out.println("Використано значення за замовчуванням: " + name);
            }

            System.out.print("Введіть email: ");
            String email = scanner.nextLine().trim().toLowerCase();
            if (email.isEmpty()) {
                email = "test.user@example.com";
                System.out.println("Використано значення за замовчуванням: " + email);
            }

            System.out.print("Введіть пароль (мінімум 8 символів): ");
            String password = scanner.nextLine();
            if (password.isEmpty()) {
                password = "securePassword123";
                System.out.println("Використано значення за замовчуванням");
            }

            User registeredUser;
            String verificationToken = null;

            try {
                // Перевірка, чи користувач вже існує
                if (authService.userExists(email)) {
                    System.out.println("\n⚠️  Користувач з email " + email + " вже існує.");
                    registeredUser = authService.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
                    
                    if (!registeredUser.isEmailVerified()) {
                        verificationToken = registeredUser.getEmailVerificationToken();
                        System.out.println("📧 Email не підтверджено. Токен доступний для підтвердження.");
                    } else {
                        System.out.println("✅ Email вже підтверджено.");
                    }
                } else {
                    // Реєстрація нового користувача
                    UserStoreDto registrationDto = new UserStoreDto(name, email, password);
                    registeredUser = authService.register(registrationDto);
                    
                    System.out.println("\n✅ Користувач успішно зареєстровано!");
                    System.out.println("   ID: " + registeredUser.getId());
                    System.out.println("   Ім'я: " + registeredUser.getName());
                    System.out.println("   Email: " + registeredUser.getEmail());
                    System.out.println("   Email підтверджено: " + registeredUser.isEmailVerified());
                    
                    // Отримуємо токен для демонстрації (в реальному застосунку він буде в email)
                    verificationToken = registeredUser.getEmailVerificationToken();
                    System.out.println("\n📧 Email з токеном підтвердження відправлено!");
                    System.out.println("   Токен підтвердження: " + verificationToken);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("\n❌ Помилка реєстрації: " + e.getMessage());
                return;
            }

            // ========== ДЕМОНСТРАЦІЯ СПРОБИ ВХОДУ БЕЗ ПІДТВЕРДЖЕННЯ ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("2. СПРОБА ВХОДУ БЕЗ ПІДТВЕРДЖЕННЯ EMAIL");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            if (!registeredUser.isEmailVerified()) {
                System.out.print("Введіть пароль для входу: ");
                String loginPassword = scanner.nextLine();
                if (loginPassword.isEmpty()) {
                    loginPassword = password;
                }

                try {
                    LoginDto loginDto = new LoginDto(email, loginPassword);
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

                System.out.println("Для підтвердження email введіть токен з email.");
                System.out.print("Токен підтвердження (або натисніть Enter для використання збереженого): ");
                String inputToken = scanner.nextLine().trim();

                String tokenToUse = inputToken.isEmpty() ? verificationToken : inputToken;

                try {
                    EmailVerificationDto verificationDto = new EmailVerificationDto(email, tokenToUse);
                    boolean verified = authService.verifyEmail(verificationDto);
                    
                    if (verified) {
                        System.out.println("\n✅ Email успішно підтверджено!");
                        System.out.println("📧 Підтвердження відправлено на email.");
                        
                        // Оновлюємо дані користувача
                        registeredUser = authService.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
                        System.out.println("   Статус підтвердження: " + registeredUser.isEmailVerified());
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("\n❌ Помилка підтвердження: " + e.getMessage());
                    
                    // Пропонуємо повторну відправку токену
                    System.out.println("\n💡 Можна запросити новий токен підтвердження.");
                    System.out.print("Запросити новий токен? (y/n): ");
                    String resend = scanner.nextLine().trim().toLowerCase();
                    
                    if (resend.equals("y") || resend.equals("yes") || resend.equals("так")) {
                        try {
                            authService.resendVerificationEmail(email);
                            System.out.println("✅ Новий токен відправлено на email!");
                            registeredUser = authService.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
                            System.out.println("   Новий токен: " + registeredUser.getEmailVerificationToken());
                        } catch (Exception ex) {
                            System.out.println("❌ Помилка: " + ex.getMessage());
                        }
                    }
                }
            }

            // ========== ДЕМОНСТРАЦІЯ УСПІШНОГО ВХОДУ ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("4. УСПІШНИЙ ВХІД ПІСЛЯ ПІДТВЕРДЖЕННЯ EMAIL");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            if (registeredUser.isEmailVerified()) {
                System.out.print("Введіть пароль для входу: ");
                String loginPassword = scanner.nextLine();
                if (loginPassword.isEmpty()) {
                    loginPassword = password;
                }

                try {
                    LoginDto loginDto = new LoginDto(email, loginPassword);
                    var loggedInUser = authService.login(loginDto);
                    
                    if (loggedInUser.isPresent()) {
                        User user = loggedInUser.get();
                        System.out.println("\n✅ ВХІД УСПІШНИЙ!");
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

            try {
                LoginDto wrongLoginDto = new LoginDto(email, "wrongPassword123");
                var failedLogin = authService.login(wrongLoginDto);
                
                if (failedLogin.isEmpty()) {
                    System.out.println("❌ Вхід не вдався: невірний пароль.");
                }
            } catch (IllegalStateException e) {
                System.out.println("❌ " + e.getMessage());
            }

            // ========== ПІДСУМОК ==========
            System.out.println("\n═══════════════════════════════════════════════════════════");
            System.out.println("ПІДСУМОК");
            System.out.println("═══════════════════════════════════════════════════════════\n");

            User finalUser = authService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));

            System.out.println("📊 Інформація про користувача:");
            System.out.println("   • ID: " + finalUser.getId());
            System.out.println("   • Ім'я: " + finalUser.getName());
            System.out.println("   • Email: " + finalUser.getEmail());
            System.out.println("   • Email підтверджено: " + (finalUser.isEmailVerified() ? "✅ Так" : "❌ Ні"));
            System.out.println("   • Токен підтвердження: " + 
                (finalUser.getEmailVerificationToken() != null ? "Є (не підтверджено)" : "Відсутній (підтверджено)"));

            System.out.println("\n✅ Демонстрація завершена успішно!");
            System.out.println("\n═══════════════════════════════════════════════════════════");

        } catch (Exception e) {
            System.out.println("\n❌ Помилка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
