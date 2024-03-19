package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class BookManagementByAdmin {

    @FXML
    private ChoiceBox<String> BookCategoryChoiceBox;
    @FXML
    private MenuItem mng_books;
    @FXML
    private MenuItem mng_categories;
    @FXML
    private MenuItem mng_users;
    @FXML
    private MenuItem mng_borrowings;
    @FXML
    private ListView<String> listView;

    public String username;
    public void setAdmin(String username) {
        this.username = username;
    }


    @FXML
    void initialize() {
        // display books' details to the ListView
        if (App.books != null) {
            showListView(App.books);
        }
        // initialize the ChoiceBox with category titles
        BookCategoryChoiceBox.getItems().add("All books"); // add this choice to display all books
        if (App.categories != null) {
            for (CategoryInfo category : App.categories) {
                BookCategoryChoiceBox.getItems().add(category.getTitle()); // add all categories' titles
            }
        }
        BookCategoryChoiceBox.setValue("All books"); // initialize choice box text
        BookCategoryChoiceBox.setOnAction(event -> {
            String selectedOption = BookCategoryChoiceBox.getValue();
            showListViewByCategory(selectedOption); // display books of the selected category
        });
    }

    public Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    private void LogOutClicked() throws IOException {
        LogOutController.Logout(primaryStage);
    }

    @FXML
    private void handleMngItemClicked(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlFileName = ""; // FXML file to load based on selected MenuItem

        switch (menuItem.getId()) {
            case "mng_books":
                fxmlFileName = "admin-logged-in.fxml";
                break;
            case "mng_categories":
                fxmlFileName = "categories-management.fxml";
                break;
            case "mng_users":
                fxmlFileName = "users-management.fxml";
                break;
            case "mng_borrowings":
                fxmlFileName = "borrowings-management.fxml";
                break;
        }

        if (!fxmlFileName.isEmpty()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
                Parent root = loader.load();
                if (fxmlFileName.equals("users-management.fxml")) {
                    UserManagementByAdmin controller = loader.getController();
                    controller.setPrimaryStage(primaryStage);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                }
                else if (fxmlFileName.equals("borrowings-management.fxml")) {
                    BorrowingsManagementByAdmin controller = loader.getController();
                    controller.setPrimaryStage(primaryStage);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                }
                else if (fxmlFileName.equals("categories-management.fxml")) {
                    CategoryManagementByAdmin controller = loader.getController();
                    controller.setPrimaryStage(primaryStage);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void EditBookClicked() {
        String selectedBook = listView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            String[] parts = selectedBook.split(", ");
            String wantedIsbn = parts[3].replaceFirst("ISBN: ", "");
            for (Book book : App.books) {
                if (book.getIsbn().equals(wantedIsbn)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-book.fxml"));
                        Parent root = loader.load();
                        EditBook controller = loader.getController();
                        controller.initialize(book);
                        controller.setPrimaryStage(primaryStage);
                        primaryStage.setScene(new Scene(root));
                        primaryStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    private void DeleteBookClicked() {
        String selectedBook = listView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            String result = ConfirmationDeleteAlert.showAlert("book");
            if (result.equals("delete")) {
                String[] parts = selectedBook.split(", ");
                String wantedIsbn = parts[3].replaceFirst("ISBN: ", "");
                Book deletebook = null;
                // Find book that selected to be deleted
                for (Book book : App.books) {
                    if (book.getIsbn().equals(wantedIsbn)) {
                        deletebook = book;
                    }
                }
                // collect users that borrowed this book
                List<User> booksusers = new ArrayList<>();
                assert deletebook != null;
                for (BorrowingInfo borrowinginfo : deletebook.userborrowings) {
                    for (User user : App.users) {
                        if (user.getUsername().equals(borrowinginfo.getUser())) {
                            booksusers.add(user);
                        }
                    }
                }
                for (User user : booksusers) {
                    user.returnBook(deletebook.getIsbn()); // remove book from user's books (basically terminate loan)
                    deletebook.returnBook(user);  // remove BorrowingInfo (user, borrowing date) from Book
                }
                // Find categoryinfo object that this book belongs to
                CategoryInfo wanted_category = null;
                for (CategoryInfo category : App.categories) {
                    if (category.getTitle().equals(deletebook.getCategory())) {
                        wanted_category = category;
                        break;
                    }
                }
                // Find book object from CategoryInfo that corresponds to book that will be deleted
                Book CategoryBook = null;
                assert wanted_category != null;
                for (Book catbook : wanted_category.getBooks()) {
                    if (catbook.getIsbn().equals(deletebook.getIsbn())) {
                        CategoryBook = catbook;
                        break;
                    }
                }
                wanted_category.removeBook(CategoryBook); // remove book from category that is contained
                App.books.remove(deletebook); // delete book
            }
            BookCategoryChoiceBox.setValue("All books");
            showListView(App.books);
        }
    }

    private void showListViewByCategory(String category) {
        ObservableList<String> bookInfoList = FXCollections.observableArrayList();
        if (App.books != null) {
            if (Objects.equals(category, "All books")) {
                showListView(App.books);
            }
            else {
                for (Book book : App.books) {
                    if (Objects.equals(book.getCategory(), category)) {
                        DecimalFormat df = new DecimalFormat("#.##");
                        String formatted_rating = df.format(book.getRating());
                        String bookInfo = "Title: " + book.getTitle() + ", " + "Author: " + book.getAuthor() +
                                ", " + "Publisher: " + book.getPublisher() + ", " + "ISBN: " + book.getIsbn() +
                                ", " + "Publication year: " + book.getYear() + ", " + "Category: " + book.getCategory() +
                                ", " + "Copies: " + book.getCopies() + ", " + "Rating: " + formatted_rating +
                                ", " + "Voters: " + book.getVoters();
                        bookInfoList.add(bookInfo);
                    }
                }
                listView.setItems(bookInfoList);
            }
        }
    }
    private void showListView(List<Book> books) {
        ObservableList<String> bookInfoList = FXCollections.observableArrayList();
        for (Book book : books) {
            DecimalFormat df = new DecimalFormat("#.##");
            String formatted_rating = df.format(book.getRating());
            String bookInfo = "Title: " + book.getTitle() + ", " + "Author: " + book.getAuthor() +
                    ", " + "Publisher: " + book.getPublisher() + ", " + "ISBN: " + book.getIsbn() +
                    ", " + "Publication year: " + book.getYear() + ", " + "Category: " + book.getCategory() +
                    ", " + "Copies: " + book.getCopies() + ", " + "Rating: " + formatted_rating +
                    ", " + "Voters: " + book.getVoters();
            bookInfoList.add(bookInfo);
        }
        listView.setItems(bookInfoList);
    }

    @FXML
    public void AddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-book.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BookCategoryChoiceBox.setValue("All books");
        if (App.books != null) {
            showListView(App.books);
        }
    }
}