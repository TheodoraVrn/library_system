package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Controller class for editing book details
 */
public class EditBook {
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
    private TextField txt_rating;
    @FXML
    private TextField txt_voters;
    @FXML
    private Button btn_comments;

    @FXML
    private Label label;

    Book book;
    Book oldbook;
    List<Book> BorrowedBooks = new ArrayList<>();

    public Stage primaryStage;

    /**
     * Sets the primary stage for the controller.
     * @param primaryStage The primary stage of the application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initializes the controller with the book details.
     * @param book The book to be edited.
     */
    public void initialize(Book book){
        this.book = book;
        this.oldbook = book; // store book in case we want to edit it
        txt_title.setText(book.getTitle());
        txt_author.setText(book.getAuthor());
        txt_publisher.setText(book.getPublisher());
        txt_isbn.setText(book.getIsbn());
        txt_year.setText(book.getYear());
        txt_copies.setText(String.valueOf(book.getCopies()));
        txt_rating.setText(String.valueOf(book.getRating()));
        txt_voters.setText(String.valueOf(book.getVoters()));

        if (App.categories != null) {
            for (CategoryInfo category : App.categories) {
                categoryChoiceBox.getItems().add(category.getTitle());
            }
        }
        categoryChoiceBox.setValue(book.getCategory());

        btn_comments.setOnAction(event -> {
            // Create a dialog to display comments
            Dialog<String> commentsDialog = new Dialog<>();
            commentsDialog.setTitle("Comments");
            commentsDialog.setHeaderText("View Comments");

            // Create a text area to display comments
            TextArea commentsTextArea = new TextArea();
            commentsTextArea.setEditable(false);
            commentsTextArea.setWrapText(true); // Enable text wrapping

            commentsTextArea.setMaxWidth(Double.MAX_VALUE);
            commentsTextArea.setMaxHeight(Double.MAX_VALUE);

            // Append comments to the text area
            for (String comment : book.getComments()) {
                commentsTextArea.appendText(comment + "\n");
            }

            // Add the text area to the dialog content
            commentsDialog.getDialogPane().setContent(commentsTextArea);

            ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
            commentsDialog.getDialogPane().getButtonTypes().add(closeButton);

            commentsDialog.showAndWait();
        });

        // find users that borrowed this book
        for (User user : App.users) {
            for (String isbn : user.isbns) {
                if (Objects.equals(isbn, book.getIsbn())) {
                    BorrowedBooks.add(book);
                }
            }
        }
    }

    /**
     * Handles the action when the user clicks the "Edit" button.
     * It updates book details and category information, and user and borrowing information if necessary.
     */
    @FXML
    private void EditClicked() {
        // take book object and change its fields
        // need to update CategoryInfo if book is moved to a new category or not
        //    --> REMOVE book from category's list of books
        //    * set Book with the updated information
        //    --> ADD book with the updated information to the same or different category
        // If users have borrowed this book and the isbn of the book is changed:
        // * remove book's old isbn from every user's list of isbns with the borrowed books
        // * add book's new isbn to every user's list of isbns
        // * update book as explained above

        String result = ConfirmationEditAlert.showAlert();
        if (result.equals("edit")) {
            String oldisbn = book.getIsbn();
            String oldcategoryTitle = book.getCategory();
            CategoryInfo oldcategory = null;
            CategoryInfo updatedcategory = null;
            // updated information for the book
            String title = txt_title.getText();
            String author = txt_author.getText();
            String publisher = txt_publisher.getText();
            String isbn = txt_isbn.getText();
            String year = txt_year.getText();
            String category = categoryChoiceBox.getValue();
            String copies = txt_copies.getText();

            // find categoryinfo object that this book belongs and the one that will be contained
            for (CategoryInfo categoryinfo : App.categories) {
                if (Objects.equals(categoryinfo.getTitle(), oldcategoryTitle)) {
                    oldcategory = categoryinfo; // category before edit
                }
                if (Objects.equals(categoryinfo.getTitle(), category)) {
                    updatedcategory = categoryinfo; // category after edit
                }
            }

            if (book.userborrowings.isEmpty()) { // no users borrowed this book
                // no userborrowings
                // nothing to change from User and BorrowingInfo classes
                // change Book and CategoryInfo
                if (!Objects.equals(oldisbn, isbn)) { // isbn is edited --> check if the new isbn is available
                    for (Book book : App.books) {
                        if (Objects.equals(book.getIsbn(), isbn)) {
                            label.setText("*This isbn is not available.");
                            return;
                        }
                    }
                }
                Book deletebook = null;
                assert oldcategory != null;
                for (Book bookincategory : oldcategory.getBooks()) {
                    if (bookincategory.getIsbn().equals(book.getIsbn())) {
                        deletebook = bookincategory; // find book that needs to be deleted from category
                    }
                }
                oldcategory.removeBook(deletebook); // remove book from category's list of books
                // update book fields
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublisher(publisher);
                book.setIsbn(isbn);
                book.setYear(year);
                book.setCategory(category);
                book.setCopies(Integer.parseInt(copies));
                updatedcategory.addBook(book); // add book to the updated category's list of books
            }
            else  { // users have borrowed this book
                if (!Objects.equals(oldisbn, isbn)) { // isbn is edited --> check if the new isbn is available
                    for (Book book : App.books) {
                        if (Objects.equals(book.getIsbn(), isbn)) {
                            label.setText("*This isbn is not available.");
                            return;
                        }
                    }
                }
                if (!Objects.equals(oldisbn, isbn)) { // isbn is edited --> remove old and add updated isbn to users' isbns
                    for (BorrowingInfo borrowinginfo : book.userborrowings) {
                        String wanteduser = borrowinginfo.getUser();
                        for (User user : App.users) {
                            if (wanteduser.equals(user.getUsername())) {
                                user.returnBook(oldisbn); // remove book with old isbn
                                user.borrowBook(isbn); // add book with the updated isbn
                            }
                        }
                    }
                }
                Book deletebook = null;
                assert oldcategory != null;
                for (Book bookincategory : oldcategory.getBooks()) {
                    if (bookincategory.getIsbn().equals(book.getIsbn())) {
                        deletebook = bookincategory; // find book that needs to be deleted from category
                    }
                }
                oldcategory.removeBook(deletebook); // remove book from category's list of books
                // update book fields
                book.setTitle(title);
                book.setAuthor(author);
                book.setPublisher(publisher);
                book.setIsbn(isbn);
                book.setYear(year);
                book.setCategory(category);
                book.setCopies(Integer.parseInt(copies));
                updatedcategory.addBook(book); // add book to the updated category's list of books
            }
            CancelClicked(); // mechanism to go back to main admin page
        }
        else {
            CancelClicked();
        }
    }

    /**
     * Handles the action when the user clicks the "Cancel" button.
     * It navigates back to the main admin page.
     */
    @FXML
    void CancelClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-logged-in.fxml"));
            Parent root = loader.load();
            BookManagementByAdmin controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
