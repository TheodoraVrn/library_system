package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogInController {

    public static List<User> users = new ArrayList<>();
    public Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;
    @FXML
    private Label txt_label;

    @FXML
    private void ExploreTopBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("top-rated-books.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToSignUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sign-up.fxml"));
        Parent root = loader.load();
        SignUpController signupController = loader.getController();
        signupController.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    void login() {
        txt_label.setText("");
        String username = txt_username.getText();
        String password = txt_password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            txt_label.setText("Please enter both username and password.");
            return;
        }

        boolean loggedIn = false;
        boolean loguser = false;
        boolean logadmin = false;

        if (App.users != null) {
            for (User user : App.users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    loguser = true;
                    loggedIn = true;
                }
            }
        }
        if (App.admins != null) {
            for (User user : App.admins) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    logadmin = true;
                    loggedIn = true;
                }
            }
        }

        try {
            if (loguser) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("user-logged-in.fxml"));
                Parent root = loader.load();
                UserLoggedIn controller = loader.getController();
                controller.setUser(username);
                controller.setPrimaryStage(primaryStage);
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle(username);
            }
            if (logadmin) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
                Parent root = loader.load();
                BookManagementByAdmin controller = loader.getController();
                controller.setAdmin(username);
                controller.setPrimaryStage(primaryStage);
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle(username);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (!loggedIn) {
            showAlert("Invalid username or password.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}