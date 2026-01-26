package smarthomesimulator.infrastructure;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailService {
    private final String fromEmail;
    private final String fromName;

    public EmailService(String fromEmail, String fromName) {
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public static EmailService createDefault() {
        return new EmailService(
            "noreply@smarthome.com",
            "Симулятор Розумного Дому"
        );
    }

    public void sendEmail(String to, String subject, String body) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("\n=== EMAIL ВІДПРАВЛЕНО ===");
        System.out.println("Мітка часу: " + timestamp);
        System.out.println("Від: " + fromName + " <" + fromEmail + ">");
        System.out.println("До: " + to);
        System.out.println("Тема: " + subject);
        System.out.println("---");
        System.out.println(body);
        System.out.println("==================\n");
    }

    public void sendRegistrationEmail(String to, String userName, String verificationToken) {
        String subject = "Ласкаво просимо до Симулятора Розумного Дому! Будь ласка, підтвердіть ваш email";
        String body = String.format(
            "Шановний(а) %s,\n\n" +
            "Дякуємо за реєстрацію в Симуляторі Розумного Дому!\n\n" +
            "Ваш обліковий запис успішно створено. Для завершення реєстрації " +
            "будь ласка підтвердіть вашу адресу електронної пошти, використовуючи наступний токен підтвердження:\n\n" +
            "Токен підтвердження: %s\n\n" +
            "Ви можете підтвердити ваш email, використовуючи цей токен в процесі підтвердження email.\n\n" +
            "Якщо ви не створювали цей обліковий запис, будь ласка, проігноруйте цей email.\n\n" +
            "З повагою,\n" +
            "Команда Симулятора Розумного Дому",
            userName,
            verificationToken
        );
        sendEmail(to, subject, body);
    }

    public void sendEmailVerificationConfirmation(String to, String userName) {
        String subject = "Email успішно підтверджено";
        String body = String.format(
            "Шановний(а) %s,\n\n" +
            "Ваша адреса електронної пошти успішно підтверджена!\n\n" +
            "Тепер ви можете повною мірою використовувати всі функції Симулятора Розумного Дому.\n\n" +
            "З повагою,\n" +
            "Команда Симулятора Розумного Дому",
            userName
        );
        sendEmail(to, subject, body);
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        String subject = "Запит на скидання пароля";
        String body = String.format(
            "Ви запросили скидання вашого пароля.\n\n" +
            "Будь ласка, використайте наступний токен для скидання пароля:\n" +
            "%s\n\n" +
            "Якщо ви не робили цей запит, будь ласка, проігноруйте цей email.\n\n" +
            "З повагою,\n" +
            "Команда Симулятора Розумного Дому",
            resetToken
        );
        sendEmail(to, subject, body);
    }
}
