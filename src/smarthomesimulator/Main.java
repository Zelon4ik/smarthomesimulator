package smarthomesimulator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import smarthomesimulator.User.User;

import java.io.FileWriter;
import java.util.List;

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
    }
}
