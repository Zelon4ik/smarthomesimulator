package smarthomesimulator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import smarthomesimulator.User.User;
import smarthomesimulator.repository.user.UserRepository;
import smarthomesimulator.unitofwork.UnitOfWork;
import smarthomesimulator.Room.Room;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) throws Exception {

        List<User> users = DataGenerator.generateUsers(5);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try (FileWriter writer = new FileWriter("smart-home-data.json")) {
            gson.toJson(users, writer);
        }

        System.out.println("Data successfully saved to smart-home-data.json");


        UserRepository repo = new UserRepository();

        User user = new User(
                "Ivan",
                "ivan@mail.com",
                new ArrayList<>()
        );

        repo.save(user);

        String id = user.getId();

        repo.findById(id);
        repo.deleteById(id);


    }
}
