package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;

public class AddCategoryPopup {

    @FXML
    private TextField categoryTitleField;
    @FXML
    private Label txt_label;

    @FXML
    void addCategory() {
        String categoryTitle = categoryTitleField.getText().trim();
        if (!categoryTitle.isEmpty()) {
            CategoryInfo category = new CategoryInfo(categoryTitle);
            if (App.categories == null) {
                App.categories = new ArrayList<>();
            }
            for (CategoryInfo categoryinfo : App.categories) {
                if (categoryinfo.getTitle().equals(categoryTitle)) {
                    txt_label.setText("*This category already exists.");
                    return;
                }
            }
            App.categories.add(category);
        }
        closePopup();
    }

    @FXML
    private void CancelClicked () {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) categoryTitleField.getScene().getWindow();
        stage.close();
    }
}
