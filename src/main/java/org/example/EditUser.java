package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditUser {
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_password;
    @FXML
    private TextField txt_firstname;
    @FXML
    private TextField txt_lastname;
    @FXML
    private TextField txt_adt;
    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_numberofbooks;
    @FXML
    private TextField txt_title1;
    @FXML
    private TextField txt_isbn1;
    @FXML
    private TextField txt_title2;
    @FXML
    private TextField txt_isbn2;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Label label;
    @FXML
    private Label user_label;

    User user;
    User olduser;
    List<Book> BorrowedBooks = new ArrayList<>();
    List<BorrowingInfo> BorrowingInfoList = new ArrayList<>();

    public Stage primaryStage;
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void initialize(User user){
        this.user = user;
        this.olduser = user; // store user in case we want to edit it
        txt_username.setText(user.getUsername());
        txt_password.setText(user.getPassword());
        txt_firstname.setText(user.getFirstname());
        txt_lastname.setText(user.getLastname());
        txt_adt.setText(user.getAdt());
        txt_email.setText(user.getEmail());

        // collect user's borrowed books
        for (String isbn : user.isbns) { // get the isbns of the user's borrowed books
            for (Book book : App.books) { // check all the books to find the ones that match the user
                if (Objects.equals(isbn, book.getIsbn())) {
                    BorrowedBooks.add(book);
                }
            }
        }
        txt_numberofbooks.setText(String.valueOf(user.getNumberofBooks()));
        if (user.getNumberofBooks() == 0) { // no books borrowed by the user
            txt_title1.setText("-");
            txt_isbn1.setText("-");
            txt_title2.setText("-");
            txt_isbn2.setText("-");
        }
        else if (user.getNumberofBooks() == 1) { // one borrowed book
            Book book1 = BorrowedBooks.get(0);
            // save borrowinginfo for user's borrowed books
            for (BorrowingInfo bookinfo : book1.userborrowings) {
                if (Objects.equals(bookinfo.getUser(), user.getUsername())) {
                    BorrowingInfoList.add(bookinfo);
                }
            }
            txt_title1.setText(book1.getTitle());
            txt_isbn1.setText(book1.getIsbn());
            txt_title2.setText("-");
            txt_isbn2.setText("-");
        }
        else { // two borrowed books
            Book book1 = BorrowedBooks.get(0);
            // save borrowinginfo for user's 1st borrowed books
            for (BorrowingInfo bookinfo : book1.userborrowings) {
                if (Objects.equals(bookinfo.getUser(), user.getUsername())) {
                    BorrowingInfoList.add(bookinfo);
                }
            }
            txt_title1.setText(book1.getTitle());
            txt_isbn1.setText(book1.getIsbn());
            Book book2 = BorrowedBooks.get(1);
            // save borrowinginfo for user's 2nd borrowed books
            for (BorrowingInfo bookinfo : book2.userborrowings) {
                if (Objects.equals(bookinfo.getUser(), user.getUsername())) {
                    BorrowingInfoList.add(bookinfo);
                }
            }
            txt_title2.setText(book2.getTitle());
            txt_isbn2.setText(book2.getIsbn());
        }
    }

    @FXML
    private void manageBooks(ActionEvent event) {
        label.setText("");
        Button button = (Button) event.getSource();
        Book book = null;
        if (user.getNumberofBooks() == 0){
            label.setText("You haven't borrowed any books");
            return;
        }
        switch (button.getId()) {
            case "button1":
                book = BorrowedBooks.get(0);
                break;
            case "button2":
                if (user.getNumberofBooks() == 1) {
                    label.setText("*You haven't borrowed a second book.");
                    return;
                }
                book = BorrowedBooks.get(1);
                break;
        }
        EndBorrowingpopup endborrowing = new EndBorrowingpopup(book, user);
        String str = endborrowing.getResult();
        if (Objects.equals(str, "terminated")) {
            CancelClicked(); // just go to the initial edit page (termination of loan completed)
        }
    }

    @FXML
    void CancelClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("users-management.fxml"));
            Parent root = loader.load();
            UserManagementByAdmin controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void EditClicked() {
        // take user object and change its fields
        // need to change userborrowings<BorrowingInfo> list for each book that user borrowed
        // - BorrowingInfoList contains userborrowings list
        // * save borrowdate for each book (max 2)
        // * delete userborrowings for each book
        // * change user's fields
        // * create new userborrowings for each book for the new user and the saved borrowdate

        String result = ConfirmationEditAlert.showAlert();
        if (result.equals("edit")) {
            String oldusername = user.getUsername();
            String old_adt = user.getAdt();
            String old_email = user.getEmail();
            // updated information for the user
            String username = txt_username.getText();
            String password = txt_password.getText();
            String firstname = txt_firstname.getText();
            String lastname = txt_lastname.getText();
            String adt = txt_adt.getText();
            String email = txt_email.getText();

            user_label.setText("");
            user_label.setTextFill(Color.RED);
            for (User user : App.users) {
                if ((user.getUsername().equals(username)) && (!Objects.equals(username, oldusername))) {
                    user_label.setText("*Username is taken.");
                    return;
                }
            }
            for (User user : App.admins) {
                if ((user.getUsername().equals(username)) && (!Objects.equals(username, oldusername))) {
                    user_label.setText("*Username is taken.");
                    return;
                }
            }
            if (username.isEmpty()) {
                user_label.setText("*Please enter username.");
                return;
            }
            if (password.isEmpty()) {
                user_label.setText("*Please enter password.");
                return;
            }
            if (firstname.isEmpty()) {
                user_label.setText("*Please enter firstname.");
                return;
            }
            if (lastname.isEmpty()) {
                user_label.setText("*Please enter lastname.");
                return;
            }
            for (User user : App.users) {
                if ((user.getAdt().equals(adt)) && (!Objects.equals(adt, old_adt))) {
                    user_label.setText("*ADT is used by another user.");
                    return;
                }
            }
            for (User user : App.admins) {
                if ((user.getAdt().equals(adt)) && (!Objects.equals(adt, old_adt))) {
                    user_label.setText("*ADT is used by another user.");
                    return;
                }
            }
            if (adt.isEmpty()) {
                user_label.setText("*Please enter ADT.");
                return;
            }
            for (User user : App.users) {
                if ((user.getEmail().equals(email)) && (!Objects.equals(email, old_email))) {
                    user_label.setText("*Email is used by another user.");
                    return;
                }
            }
            for (User user : App.admins) {
                if ((user.getEmail().equals(email)) && (!Objects.equals(email, old_email))) {
                    user_label.setText("*Email is used by another user.");
                    return;
                }
            }
            if (email.isEmpty()) {
                user_label.setText("*Please enter email.");
                return;
            }
            //updateRateData(oldusername);

            LocalDate borrowdate1;
            LocalDate borrowdate2;
            if (BorrowingInfoList.isEmpty()) {
                // user hasn't borrowed any books
                // nothing to change from Book and BorrowingInfo classes
                user.setUsername(username);
                user.setPassword(password);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setAdt(adt);
                user.setEmail(email);
            } else if (BorrowingInfoList.size() == 1) { // user borrowed one book
                BorrowingInfo bookinfo = BorrowingInfoList.get(0);
                borrowdate1 = bookinfo.getBorrowDate();
                Book book = BorrowedBooks.get(0);
                user.returnBook(book.getIsbn()); // remove book from user's borrowed books (User object)
                book.returnBook(user); // remove BorrowingInfo (user, date) from Book object

                // update user
                user.setUsername(username);
                user.setPassword(password);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setAdt(adt);
                user.setEmail(email);

                user.borrowBook(book.getIsbn());
                book.updateBorrowingInfo(borrowdate1, user);
            } else { // user borrowed two books
                updateRateData(oldusername);
                BorrowingInfo bookinfo1 = BorrowingInfoList.get(0);
                borrowdate1 = bookinfo1.getBorrowDate();
                Book book1 = BorrowedBooks.get(0);
                user.returnBook(book1.getIsbn()); // remove book from user's borrowed books (User object)
                book1.returnBook(user); // remove BorrowingInfo (user, date) from Book object

                BorrowingInfo bookinfo2 = BorrowingInfoList.get(1);
                borrowdate2 = bookinfo2.getBorrowDate();
                Book book2 = BorrowedBooks.get(1);
                user.returnBook(book2.getIsbn()); // remove book from user's borrowed books (User object)
                book2.returnBook(user); // remove BorrowingInfo (user, date) from Book object

                // update user
                user.setUsername(username);
                user.setPassword(password);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                user.setAdt(adt);
                user.setEmail(email);

                // Add the book to the user's list of borrowed books
                user.borrowBook(book1.getIsbn());
                user.borrowBook(book2.getIsbn());
                // add borrowing info to the list from Book class
                book1.updateBorrowingInfo(borrowdate1, user);
                book2.updateBorrowingInfo(borrowdate2, user);
            }
            updateRateData(oldusername);
            user_label.setText("User was modified successfully.");
            user_label.setTextFill(Color.GREEN);
        }
        else {
            CancelClicked();
        }
    }

    private void updateRateData(String oldusername) {
        if (App.books != null) {
            for (Book book : App.books) {
                // update rating history with the new user's info
                book.updateRate(oldusername, user);
            }
        }
    }
}
