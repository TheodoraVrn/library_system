package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;

public class AddAdminPopup {
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

    @FXML
    public void AddAdminClicked(ActionEvent event) {
        txt_label.setText("");
        txt_label.setTextFill(Color.RED);
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

        // add admin to the admins list
        User admin = new User(username, password, firstname, lastname, adt, email, 0, new ArrayList<>());
        App.admins.add(admin);
        txt_username.setText("");
        txt_password.setText("");
        txt_firstname.setText("");
        txt_lastname.setText("");
        txt_adt.setText("");
        txt_email.setText("");
        txt_label.setText("Admin added successfully.");
        txt_label.setTextFill(Color.GREEN);
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) txt_username.getScene().getWindow();
        stage.close();
    }
}
