package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UserLoggedIn {

    public static List<Book> books = new ArrayList<>();

    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label borrowing_label;

    public String username;
    public void setUser(String username) {
        this.username = username;
    }

    public Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    void initialize() {
        // Display books to the ListView
        if (App.books != null) {
            showListView(App.books);
        }
    }

    @FXML
    private void LogOutClicked() throws IOException {
        LogOutController.Logout(primaryStage);
    }

    private void showListView(List<Book> books) {
        ObservableList<String> bookInfoList = FXCollections.observableArrayList();
        for (Book book : books) {
            DecimalFormat df = new DecimalFormat("#.##");
            String formatted_rating = df.format(book.getRating());
            String bookInfo = "Title: " + book.getTitle() + ", " + "Author: " + book.getAuthor() +
                    ", " + "ISBN: " + book.getIsbn() + ", " + "Rating: " + formatted_rating + ", " +
                    "Voters: " + book.getVoters();
            bookInfoList.add(bookInfo);
        }
        listView.setItems(bookInfoList);
    }

    @FXML
    private void showAllBooks(){
        borrowing_label.setText("");
        searchField.setText("");  // Empty the Search field
        if (App.books != null) {
            showListView(App.books);
        }
    }

    @FXML
    private void borrowBook() {
        String selectedBook = listView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            String[] parts = selectedBook.split(", ");
            String wantedIsbn = parts[2].replaceFirst("ISBN: ", "");
            for (Book book : App.books) {
                if (book.getIsbn().equals(wantedIsbn)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("borrow-book.fxml"));
                        Parent root = loader.load();
                        BorrowBook controller = loader.getController();
                        controller.setUser(username);
                        controller.initialize(book);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    private void OpenBorrowings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("open-borrowings.fxml"));
            Parent root = loader.load();
            OpenBorrowings controller = loader.getController();
            controller.setUser(username);
            controller.setPrimaryStage(primaryStage);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchBooks() {
        String keyword = searchField.getText().toLowerCase();
        List<Book> searchResults = new ArrayList<>();
        for (Book book : App.books) {
            if (book.getTitle().toLowerCase().contains(keyword) ||
                    book.getAuthor().toLowerCase().contains(keyword) ||
                    book.getYear().toLowerCase().contains(keyword)) {
                searchResults.add(book);
            }
        }
        showListView(searchResults);
    }

    @FXML
    void showDetails() {
        String selectedBook = listView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            String[] parts = selectedBook.split(", ");
            String wantedIsbn = parts[2].replaceFirst("ISBN: ", "");
            // Load the pop-up window with book details
            for (Book book : App.books) {
                if (book.getIsbn().equals(wantedIsbn)) {
                    new BookDetailspopup(book, username);
                }
            }
        }
    }
}
