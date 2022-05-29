package ru.javabegin.BookStore;

import com.google.gson.Gson;
import ru.javabegin.BookStore.entity.MainServer;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonParser {

    public MainServer FromJson(){
        Gson g = new Gson();
        try (FileReader fileReader = new FileReader("data.json")) {
            return g.fromJson(fileReader, MainServer.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ToJson(MainServer mainserver) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("data.json")) {
            gson.toJson(mainserver, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
