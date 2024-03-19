package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;

public class BorrowBook {
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
    private TextField txt_category;
    @FXML
    private TextField txt_copies;
    @FXML
    private TextField txt_rating;
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_numberbooks;
    @FXML
    private TextField txt_borrowingdate;
    @FXML
    private TextField txt_returndate;
    @FXML
    private Label txt_label;

    LocalDate today = LocalDate.now();
    LocalDate returnDate = today.plusDays(5);

    public String username;
    User user;
    Book book;

    public void setUser(String username) {
        this.username = username;
        txt_username.setText(username);
        // find user object from string username
        for (User user : App.users) {
            if (user.getUsername().equals(username)) {
                this.user = user;
            }
        }
        txt_numberbooks.setText(String.valueOf(user.getNumberofBooks()));
        txt_borrowingdate.setText(String.valueOf(today));
        txt_returndate.setText(String.valueOf(returnDate));
    }

    public void initialize(Book book){
        this.book = book;
        txt_title.setText(book.getTitle());
        txt_author.setText(book.getAuthor());
        txt_publisher.setText(book.getPublisher());
        txt_isbn.setText(book.getIsbn());
        txt_year.setText(book.getYear());
        txt_category.setText(book.getCategory());
        txt_copies.setText(book.getCopies().toString());
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted_rating = df.format(book.getRating());
        txt_rating.setText(formatted_rating);
    }

    @FXML
    void OnclickBorrowBook() {
        if (user.getNumberofBooks() == 2) {
            txt_label.setText("*You have reached the maximum borrowing limit.");
            return;
        }
        // Check if the user has already borrowed the selected book
        for (BorrowingInfo bookinfo : book.userborrowings) {
            if (Objects.equals(bookinfo.getUser(), username)) {
                txt_label.setText("*You have already borrowed this book.");
                return;
            }
        }
        if (book.getCopies() == 0) {
            txt_label.setText("*Sorry, no available copies.");
            return;
        }
        // Borrow the book
        user.borrowBook(book.getIsbn()); // Add the book to the user's list of borrowed books
        book.setCopies(book.getCopies() - 1); // Reduce the available copies of the book by 1
        book.borrowBook(user); // add borrowing info to the book

        txt_label.setTextFill(Color.GREEN);
        txt_label.setText("*Book borrowed successfully.");
    }
}
