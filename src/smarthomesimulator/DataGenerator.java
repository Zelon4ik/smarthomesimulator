package smarthomesimulator;

import net.datafaker.Faker;
import smarthomesimulator.Device.Device;
import smarthomesimulator.Room.Room;
import smarthomesimulator.User.User;
import smarthomesimulator.infrastructure.PasswordHasher;

import java.util.*;

public class DataGenerator {

    public static List<User> generateUsers(int count) {
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            Device device = new Device(
                    UUID.randomUUID().toString(),
                    faker.device().modelName(),
                    "Light",
                    true,
                    faker.number().randomDouble(2, 10, 100)
            );

            Room room = new Room(
                    UUID.randomUUID().toString(),
                    faker.house().room(),
                    List.of(device)
            );

            users.add(new User(
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    PasswordHasher.hashPassword("defaultPassword123"), // Default password for generated users
                    List.of(room)
            ));
        }

        return users;
    }
}
