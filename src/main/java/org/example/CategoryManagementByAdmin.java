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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryManagementByAdmin {
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
        // display categories' titles to the ListView
        if (App.categories != null) {
            showListView(App.categories);
        }
    }

    @FXML
    private void LogOutClicked() throws IOException {
        LogOutController.Logout(primaryStage);
    }
    @FXML
    public void AddCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add-category-popup.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (App.categories != null) {
            showListView(App.categories);
        }
    }

    @FXML
    public void EditCategory() {
        String selectedCategory = listView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-category-popup.fxml"));
                Parent root = loader.load();
                EditCategoryPopup controller = loader.getController();
                controller.setCategory(selectedCategory);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
            showListView(App.categories);
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
                else if (fxmlFileName.equals("borrowings-management.fxml")) {
                    BorrowingsManagementByAdmin controller = loader.getController();
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
    private void DeleteCategory() {
        String selectedCategory = listView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            String result = ConfirmationDeleteAlert.showAlert("category");
            if (result.equals("delete")) {
                CategoryInfo deletecategory = null;
                // Find category that is selected to be deleted
                for (CategoryInfo category : App.categories) {
                    if (category.getTitle().equals(selectedCategory)) {
                        deletecategory = category;
                    }
                }
                List<Book> CategoryBooks = new ArrayList<>();
                if (App.books != null) {
                    for (Book book : App.books) { // check all the books to find the ones that belongs to the category
                        if (Objects.equals(selectedCategory, book.getCategory())) {
                            CategoryBooks.add(book);
                        }
                    }
                    // delete books and terminate all open loans for that books
                    List<User> CategoryUsers = new ArrayList<>();
                    List<String> booksIsbns = new ArrayList<>();
                    for (Book book : CategoryBooks) {
                        for (BorrowingInfo bookinfo : book.userborrowings) {
                            for (User user : App.users) {
                                if (user.getUsername().equals(bookinfo.getUser())){
                                    CategoryUsers.add(user);         // users that have open loans with category's books
                                    booksIsbns.add(book.getIsbn());  // books' isbns that belong to the category
                                }
                            }
                        }
                    }
                    for (User user : CategoryUsers) {
                        for (String isbn : booksIsbns) {
                            if (user.isbns.contains(isbn)) {
                                user.returnBook(isbn); // terminate user's open loans for category's books
                            }
                        }
                    }
                    for (Book book : CategoryBooks) {
                        updateBookData(book);  // delete books that belong to the category
                    }
                }
                updateCategoryData(deletecategory); // delete category
            }
            showListView(App.categories);
        }
    }

    private void showListView(List<CategoryInfo> categories) {
        ObservableList<String> categoryInfoList = FXCollections.observableArrayList();
        for (CategoryInfo category : categories) {
            String categoryInfo = category.getTitle();
            categoryInfoList.add(categoryInfo);
        }
        listView.setItems(categoryInfoList);
    }
    private void updateBookData(Book book) {
        App.books.removeIf(b -> b.getIsbn().equals(book.getIsbn())); // Remove the book object
    }

    private void updateCategoryData(CategoryInfo category) {
        App.categories.removeIf(c -> c.getTitle().equals(category.getTitle())); // Remove the category object
    }
}
