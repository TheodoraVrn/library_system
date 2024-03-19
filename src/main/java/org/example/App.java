package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    public static List<User> users = new ArrayList<>();
    public static List<User> admins = new ArrayList<>();
    public static List<Book> books = new ArrayList<>();
    public static List<CategoryInfo> categories = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("log-in.fxml"));
        Parent root = loader.load();
        LogInController loginController = loader.getController();
        loginController.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private static void deserialize() {
        // Deserialize user and admin data
        users = (List<User>)deserializeUsers("./medialab/users-credentials.ser");
        admins = (List<User>)deserializeUsers("./medialab/admin-credentials.ser");

        // Deserialize book data
        books = (List<Book>) deserializeBooks("./medialab/books.ser");

        // Deserialize category data
        categories = (List<CategoryInfo>)deserializeCategories("./medialab/categories.ser");
    }
    private static List<User> deserializeUsers(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<Book> deserializeBooks(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Book>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<CategoryInfo> deserializeCategories(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<CategoryInfo>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void serializeUsers(String fileName, List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void serializeBooks(String fileName, List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(books);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void serializeCategories(String fileName, List<CategoryInfo> categories) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // object deserialization when the program starts
        deserialize();
        launch();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Serialize users, admins, books, categories objects when the program stops
            serializeUsers("./medialab/users-credentials.ser", users);
            serializeUsers("./medialab/admin-credentials.ser", admins);
            serializeBooks("./medialab/books.ser", books);
            serializeCategories("./medialab/categories.ser", categories);
        }));
    }
}