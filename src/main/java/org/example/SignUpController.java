package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.*;

public class SignUpController {
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;
    @FXML
    private TextField txt_firstname;
    @FXML
    private TextField txt_lastname;
    @FXML
    private TextField txt_adt;
    @FXML
    private TextField txt_email;
    @FXML
    private Label txt_label;

    public Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

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
    private void switchToLogIn() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("log-in.fxml"));
        Parent root = loader.load();
        LogInController logInController = loader.getController();
        logInController.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    public void signUp() {
        txt_label.setText("");
        String username = txt_username.getText();
        String password = txt_password.getText();
        String firstname = txt_firstname.getText();
        String lastname = txt_lastname.getText();
        String adt = txt_adt.getText();
        String email = txt_email.getText();

        if (App.users == null) {
            App.users = new ArrayList<>();
        }
        if (App.admins == null) {
            App.admins = new ArrayList<>();
        }

        for (User user : App.users) {
            if (user.getUsername().equals(username)) {
                txt_label.setText("*Username is taken.");
                return;
            }
        }
        for (User user : App.admins) {
            if (user.getUsername().equals(username)) {
                txt_label.setText("*Username is taken.");
                return;
            }
        }
        if (username.isEmpty()) {
            txt_label.setText("*Please enter username.");
            return;
        }
        if (password.isEmpty()) {
            txt_label.setText("*Please enter password.");
            return;
        }
        if (firstname.isEmpty()) {
            txt_label.setText("*Please enter firstname.");
            return;
        }
        if (lastname.isEmpty()) {
            txt_label.setText("*Please enter lastname.");
            return;
        }
        for (User user : App.users) {
            if (user.getAdt().equals(adt)) {
                txt_label.setText("*ADT is used by another user.");
                return;
            }
        }
        for (User user : App.admins) {
            if (user.getAdt().equals(adt)) {
                txt_label.setText("*ADT is used by another user.");
                return;
            }
        }
        if (adt.isEmpty()) {
            txt_label.setText("*Please enter ADT.");
            return;
        }
        for (User user : App.users) {
            if (user.getEmail().equals(email)) {
                txt_label.setText("*Email is used by another user.");
                return;
            }
        }
        for (User user : App.admins) {
            if (user.getEmail().equals(email)) {
                txt_label.setText("*Email is used by another user.");
                return;
            }
        }
        if (email.isEmpty()) {
            txt_label.setText("*Please enter email.");
            return;
        }

        // add user to the users list
        User user = new User(username, password, firstname, lastname, adt, email, 0, new ArrayList<>());
        //App.admins.add(user); // used to add the first admin
        App.users.add(user);
        txt_username.setText("");
        txt_password.setText("");
        txt_firstname.setText("");
        txt_lastname.setText("");
        txt_adt.setText("");
        txt_email.setText("");
        showAlert("Signed up successfully.");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}