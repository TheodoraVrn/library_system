package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddBookPopup {
    @FXML
    private TextField txt_title;
    @FXML
    private TextField txt_author;
    @FXML
    private TextField txt_publisher;
    @FXML
    private TextField txt_isbn;
    @FXML
    private TextField txt_year;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private TextField txt_copies;
    @FXML
    private Label txt_label;

    @FXML
    void initialize() {
        if (App.categories != null) {
            for (CategoryInfo category : App.categories) {
                categoryChoiceBox.getItems().add(category.getTitle()); // add category titles to choice box
            }
        }
        // initialize choice box text
        categoryChoiceBox.setValue("Category");
    }

    @FXML
    public void AddBook() {
        String title = txt_title.getText();
        String author = txt_author.getText();
        String publisher = txt_publisher.getText();
        String isbn = txt_isbn.getText();
        String year = txt_year.getText();
        String copies = txt_copies.getText();
        String selectedCategory = categoryChoiceBox.getValue();

        txt_label.setText("");
        txt_label.setTextFill(Color.RED);
        if (title.isEmpty()) {
            txt_label.setText("*Please enter title.");
            return;
        }
        if (author.isEmpty()) {
            txt_label.setText("*Please enter author.");
            return;
        }
        if (publisher.isEmpty()) {
            txt_label.setText("*Please enter publisher.");
            return;
        }
        if (isbn.isEmpty()) {
            txt_label.setText("*Please enter isbn.");
            return;
        }
        if (App.books != null) {
            for (Book book : App.books) {
                if (Objects.equals(book.getIsbn(), isbn)) {
                    txt_label.setText("*This isbn is not available.");
                    return;
                }
            }
        }
        if (year.isEmpty()) {
            txt_label.setText("*Please enter publication year.");
            return;
        }
        if (Objects.equals(selectedCategory, "Category")) {
            txt_label.setText("*Please enter category.");
            return;
        }
        if (copies.isEmpty()) {
            txt_label.setText("*Please enter number of copies.");
            return;
        }
        Book book = new Book(title, author, publisher, isbn, year, selectedCategory,
                Integer.valueOf(copies), 0,0, new HashMap<>(), new ArrayList<>(), new ArrayList<>());
        if (App.books == null) {
            App.books = new ArrayList<>();
        }
        App.books.add(book);
        for (CategoryInfo category : App.categories) { // add book to the category's list of books
            if (category.getTitle().equals(book.getCategory())) {
                category.addBook(book);
            }
        }
        txt_label.setText("Book added successfully.");
        txt_label.setTextFill(Color.GREEN);
        txt_title.setText("");
        txt_author.setText("");
        txt_publisher.setText("");
        txt_isbn.setText("");
        txt_year.setText("");
        txt_copies.setText("");
        categoryChoiceBox.setValue("Category");
    }
}


