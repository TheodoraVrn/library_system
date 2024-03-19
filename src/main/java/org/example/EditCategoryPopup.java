package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditCategoryPopup {

    @FXML
    private TextField categoryTitleField;
    @FXML
    private Label txt_label;

    CategoryInfo CategoryForEdit;
    String previoustitle;

    public void setCategory(String categoryTitle){
        previoustitle = categoryTitle;
        // find category object based on category title
        for (CategoryInfo categoryinfo : App.categories) {
            if (categoryinfo.getTitle().equals(categoryTitle)) {
                CategoryForEdit = categoryinfo;
            }
        }
        categoryTitleField.setText(categoryTitle);
    }

    @FXML
    private void editCategory() {
        String newcategoryTitle = categoryTitleField.getText();
        if (!newcategoryTitle.isEmpty() && !newcategoryTitle.equals(previoustitle)) {
            for (CategoryInfo categoryinfo : App.categories) { // check if the new category title is taken
                if (categoryinfo.getTitle().equals(newcategoryTitle)) {
                    txt_label.setText("*This category already exists.");
                    return;
                }
            }
            List<Book> CategoriesBooks = new ArrayList<>();
            if (App.books != null) {
                for (Book book : App.books) { // check all the books to find the ones that belongs to the category
                    if (Objects.equals(CategoryForEdit.getTitle(), book.getCategory())) {
                        CategoriesBooks.add(book);
                    }
                }
                for (Book book : CategoriesBooks) {
                    book.setCategory(newcategoryTitle); // update book's category
                }
            }
            CategoryForEdit.setTitle(newcategoryTitle); // update category title
            closePopup();

        }
        txt_label.setText("*This category already exists.");
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
