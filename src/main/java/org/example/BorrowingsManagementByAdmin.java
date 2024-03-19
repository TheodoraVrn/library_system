package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class BorrowingsManagementByAdmin {
    @FXML
    private ListView<String> listView;
    @FXML
    private MenuItem mng_books;
    @FXML
    private MenuItem mng_categories;
    @FXML
    private MenuItem mng_users;
    @FXML
    private MenuItem mng_borrowings;


    public Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void initialize() {
        // display books' details to the ListView
        if (App.books != null) {
            showBorrowingsListView(App.books);
        }
    }

    @FXML
    private void LogOutClicked() throws IOException {
        LogOutController.Logout(primaryStage);
    }

    @FXML
    private void manageBorrowings() {
        String selectedBook = listView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            String[] parts = selectedBook.split(", ");
            String wantedUsername = parts[0].replaceFirst("Username: ", "");
            String wantedIsbn = parts[5].replaceFirst("ISBN: ", "");
            User wanteduser = null;
            // find User
            for (User user : App.users) {
                if (user.getUsername().equals(wantedUsername)) {
                    wanteduser = user;
                }
            }
            // find Book
            for (Book book : App.books) {
                if (book.getIsbn().equals(wantedIsbn)) {
                    // Load the pop-up window with loan details and termination loan button
                    new EndBorrowingpopup(book, wanteduser);
                }
            }
        }
        if (App.books != null) {
            showBorrowingsListView(App.books);
        }
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
                else if (fxmlFileName.equals("admin-logged-in.fxml")) {
                    BookManagementByAdmin controller = loader.getController();
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

    private void showBorrowingsListView(List<Book> books) {
        ObservableList<String> bookInfoList = FXCollections.observableArrayList();
        for (Book book : books) {
            for (BorrowingInfo bookinfo : book.userborrowings) {
                String bookInfo = "Username: " + bookinfo.getUser() + ", " +
                        "Borrowing Date: " + bookinfo.getBorrowDate() + ", " +
                        "Returning Date: " + bookinfo.getReturnDate() + ", " +
                        "Title: " + book.getTitle() + ", " + "Author: " + book.getAuthor() + ", " +
                        "ISBN: " + book.getIsbn() + ", " + "Rating: " + book.getRating();
                bookInfoList.add(bookInfo);
            }
        }
        listView.setItems(bookInfoList);
    }
}
