package org.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Objects;
import java.util.Optional;

public class ConfirmationDeleteAlert {
    static String showAlert(String object) {
        // Create an alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Window");
        if (Objects.equals(object, "book")) {
            alert.setHeaderText("Are you sure you want to delete the selected book?");
        }
        else if (Objects.equals(object, "category")) {
            alert.setHeaderText("Are you sure you want to delete the selected category?");
        }
        else if (Objects.equals(object, "user")) {
            alert.setHeaderText("Are you sure you want to delete the selected user?");
        }

        // Add OK and Cancel buttons
        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        // Handle the user's response
        if (result.isPresent() && result.get() == okButton) {
            return "delete";
        } else {
            return "cancel";
        }
    }
}
