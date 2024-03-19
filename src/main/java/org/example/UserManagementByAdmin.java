package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserManagementByAdmin {
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
    @FXML
    private Label admin_label;

    public static List<User> users = new ArrayList<>();

    public Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void initialize() {
        // Display users' details to the ListView
        showListView(App.users);
    }

    @FXML
    private void ViewUsersClicked() {
        // Display users' details to the ListView
        showListView(App.users);
        admin_label.setText("");
    }

    @FXML
    private void ViewAdminsClicked() {
        // Display admins' details to the ListView
        showListView(App.admins);
        admin_label.setText("");
    }

    @FXML
    private void LogOutClicked() throws IOException {
        LogOutController.Logout(primaryStage);
    }

    @FXML
    public void AddAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-admin.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        showListView(App.admins);
        admin_label.setText("");
    }

    @FXML
    private void DeleteClicked() {
        admin_label.setText("");
        String stageTitle = primaryStage.getTitle(); // stores the admin that has logged in
        String selectedUser = listView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String result = ConfirmationDeleteAlert.showAlert("user");
            if (result.equals("delete")) {
                String[] parts = selectedUser.split(", ");
                String wantedUsername = parts[0].replaceFirst("Username: ", "");
                for (User admin : App.admins) {
                    if (admin.getUsername().equals(wantedUsername) && !wantedUsername.equals(stageTitle)) {
                        //admin_label.setText("*You cannot delete an admin.");
                        App.admins.remove(admin);
                        showListView(App.admins);
                        return;
                    }
                    else if (wantedUsername.equals(stageTitle)) {
                        admin_label.setText("*You cannot delete your account. Another admin should do it.");
                        return;
                    }
                }
                User deleteuser = null;
                // Find user that is selected to be deleted
                for (User user : App.users) {
                    if (user.getUsername().equals(wantedUsername)) {
                        deleteuser = user;
                    }
                }
                List<Book> BorrowedBooks = new ArrayList<>();
                // collect user's borrowed books
                for (String isbn : deleteuser.isbns) { // get the isbns of the user's borrowed books
                    for (Book book : App.books) { // check all the books to find the ones that match the user
                        if (Objects.equals(isbn, book.getIsbn())) {
                            BorrowedBooks.add(book);
                        }
                    }
                }
                for (Book book : BorrowedBooks) {
                    book.setCopies(book.getCopies() + 1);
                    book.returnBook(deleteuser);  // remove BorrowingInfo (user, date) from Book object
                }
                App.users.remove(deleteuser); // delete user
            }
            showListView(App.users);
        }
    }

    @FXML
    private void EditUserClicked() {
        admin_label.setText("");
        String selectedUser = listView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String[] parts = selectedUser.split(", ");
            String wantedUsername = parts[0].replaceFirst("Username: ", "");
            for (User admin : App.admins) {
                if (admin.getUsername().equals(wantedUsername)) {
                    admin_label.setText("*You cannot edit an admin.");
                    return;
                }
            }
            for (User user : App.users) {
                if (user.getUsername().equals(wantedUsername)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-user.fxml"));
                        Parent root = loader.load();
                        EditUser controller = loader.getController();
                        controller.initialize(user);
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
    private void handleMngItemClicked(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlFileName = ""; // FXML file to load based on selected MenuItem
        switch (menuItem.getId()) {
            case "mng_books":
                //fxmlFileName = "books-management.fxml";
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
                if (fxmlFileName.equals("admin-logged-in.fxml")) {
                    BookManagementByAdmin controller = loader.getController();
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

    private void showListView(List<User> users) {
        ObservableList<String> userInfoList = FXCollections.observableArrayList();
        for (User user : users) {
            String userInfo = "Username: " + user.getUsername() + ", " + "Password: " + user.getPassword() +
                    ", " + "FirstName: " + user.getFirstname() +
                    ", " + "LastName: " + user.getLastname() + ", " + "ADT: " + user.getAdt() +
                    ", " + "Email: " + user.getEmail();
            userInfoList.add(userInfo);
        }
        listView.setItems(userInfoList);
    }
}
