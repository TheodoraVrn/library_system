package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class OpenBorrowings {
    public String username;
    User user;
    List<Book> BorrowedBooks = new ArrayList<>();

    @FXML
    private TextField txt_title1;
    @FXML
    private TextField txt_title2;
    @FXML
    private TextField txt_isbn1;
    @FXML
    private TextField txt_isbn2;
    @FXML
    private TextField txt_year1;
    @FXML
    private TextField txt_year2;
    @FXML
    private TextField txt_borrowingdate1;
    @FXML
    private TextField txt_borrowingdate2;
    @FXML
    private TextField txt_returningdate1;
    @FXML
    private TextField txt_returningdate2;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Button rate1;
    @FXML
    private Button rate2;
    @FXML
    private Button comment1;
    @FXML
    private Button comment2;

    public Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUser(String username) {
        this.username = username;
        // find user object from string username
        for (User user : App.users) {
            if (user.getUsername().equals(username)) {
                this.user = user;
            }
        }
        setBorrowedBooks();
    }

    public void setBorrowedBooks() {
        for (String isbn : user.isbns) { // get the isbns of the user's borrowed books
            for (Book book : App.books) { // check all the books to find the ones that match the user
                if (Objects.equals(isbn, book.getIsbn())) {
                    BorrowedBooks.add(book);
                }
            }
        }
        if (user.getNumberofBooks() == 0) {
            label1.setText("*You haven't borrowed a book.");
            label2.setText("*You haven't borrowed a book.");
        }
        // user borrowed two books
        else if (user.getNumberofBooks() == 2) {
            Book book1 = BorrowedBooks.get(0);
            for (BorrowingInfo bookinfo1 : book1.userborrowings) {
                if (Objects.equals(bookinfo1.getUser(), username)) {
                    txt_borrowingdate1.setText(bookinfo1.getBorrowDate().toString());
                    txt_returningdate1.setText(bookinfo1.getReturnDate().toString());
                }
            }
            txt_title1.setText(book1.getTitle());
            txt_isbn1.setText(book1.getIsbn());
            txt_year1.setText(book1.getYear());
            Book book2 = BorrowedBooks.get(1); // get the second borrowed book
            // Associate book with the user
            for (BorrowingInfo bookinfo2 : book2.userborrowings) {
                if (Objects.equals(bookinfo2.getUser(), username)) {
                    txt_borrowingdate2.setText(bookinfo2.getBorrowDate().toString());
                    txt_returningdate2.setText(bookinfo2.getReturnDate().toString());
                }
            }
            txt_title2.setText(book2.getTitle());
            txt_isbn2.setText(book2.getIsbn());
            txt_year2.setText(book2.getYear());
        }
        else { // user borrowed one book
            label2.setText("*You can borrow one more book.");
            Book book = BorrowedBooks.get(0);
            for (BorrowingInfo bookinfo : book.userborrowings) {
                if (Objects.equals(bookinfo.getUser(), username)) {
                    txt_borrowingdate1.setText(bookinfo.getBorrowDate().toString());
                    txt_returningdate1.setText(bookinfo.getReturnDate().toString());
                }
            }
            txt_title1.setText(book.getTitle());
            txt_isbn1.setText(book.getIsbn());
            txt_year1.setText(book.getYear());
        }
    }

    @FXML
    void rateBook(ActionEvent event) {
        if (user.getNumberofBooks() != 0) {
            Button button = (Button) event.getSource();
            Book book = null;
            Label label = null;
            switch (button.getId()) {
                case "rate1":
                    book = BorrowedBooks.get(0);
                    label = label1;
                    break;
                case "rate2":
                    if (user.getNumberofBooks() == 1) {
                        label2.setText("*You haven't borrowed a second book.");
                        return;
                    }
                    book = BorrowedBooks.get(1);
                    label = label2;
                    break;
            }
            ToggleGroup ratingGroup = new ToggleGroup();

            // Create radio buttons for rating options (1 to 5)
            RadioButton[] ratingButtons = new RadioButton[5];
            for (int i = 0; i < 5; i++) {
                int ratingValue = i + 1;
                ratingButtons[i] = new RadioButton(Integer.toString(ratingValue));
                ratingButtons[i].setToggleGroup(ratingGroup);
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Rate Book");
            alert.setHeaderText("Choose a rating:");
            alert.getButtonTypes().clear(); // Clear default button types

            // Add radio buttons to the dialog content
            alert.getDialogPane().setContent(new VBox(ratingButtons));

            alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the dialog and handle the selected rating
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                RadioButton selectedButton = (RadioButton) ratingGroup.getSelectedToggle();
                if (selectedButton != null) {
                    int rating = Integer.parseInt(selectedButton.getText());
                    // Handle the selected rating (e.g., update the book's rating)
                    int rated = updateBookRating(rating, book);
                    if (rated == 0) { // user has already rated the book, the new rate is not valid
                        label.setText("*You have already rated this book.");
                    }
                }
            }
        }
    }

    @FXML
    private void AddComment(ActionEvent event) {
        if (user.getNumberofBooks() != 0) {
            Button button = (Button) event.getSource();
            Book book = null;
            switch (button.getId()) {
                case "comment1":
                    book = BorrowedBooks.get(0);
                    break;
                case "comment2":
                    if (user.getNumberofBooks() == 1) {
                        label2.setText("*You haven't borrowed a second book.");
                        return;
                    }
                    book = BorrowedBooks.get(1);
                    break;
            }
            TextArea textArea = new TextArea();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add Comments");
            alert.setHeaderText("Enter your comments:");
            alert.getButtonTypes().clear(); // Clear default button types

            // Add the text area to the dialog content
            alert.getDialogPane().setContent(textArea);

            alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the dialog and handle the entered comments
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                StringBuilder comments = new StringBuilder();
                comments.append(user.getUsername()).append(": ").append(textArea.getText());
                updateBookComments(comments.toString(), book);
            }
        }
    }

    @FXML
    private void goBack(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user-logged-in.fxml"));
            Parent root = loader.load();
            UserLoggedIn controller = loader.getController();
            controller.setUser(username);
            controller.setPrimaryStage(primaryStage);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer updateBookRating(int rating, Book book){
        return book.rateBook(user.getUsername(), rating);
    }

    private void updateBookComments(String comment, Book book){
        book.addComment(comment);
    }
}
