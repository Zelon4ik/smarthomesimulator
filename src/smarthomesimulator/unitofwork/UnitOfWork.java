package smarthomesimulator.unitofwork;

import com.google.gson.*;
import smarthomesimulator.User.User;

import java.io.FileWriter;
import java.util.List;

public class UnitOfWork {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void commitUsers(List<User> users) {
        try (FileWriter writer = new FileWriter("smart-home-data.json")) {
            gson.toJson(users, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
