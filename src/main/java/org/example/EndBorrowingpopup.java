package org.example;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;

public class EndBorrowingpopup {
    private String result;
    public String getResult() {
        return this.result;
    }
    public EndBorrowingpopup(Book book, User user) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText("Loan Details");

        String borrowingdate = "";
        String returningdate = "";

        for (BorrowingInfo bookinfo : book.userborrowings) {
            if (Objects.equals(bookinfo.getUser(), user.getUsername())) {
                borrowingdate = bookinfo.getBorrowDate().toString();
                returningdate = bookinfo.getReturnDate().toString();
            }
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

        TextField usernameField = new TextField();
        usernameField.setEditable(false);
        usernameField.setText(user.getUsername());

        TextField borrowdateField = new TextField();
        borrowdateField.setEditable(false);
        borrowdateField.setText(borrowingdate);

        TextField returndateField = new TextField();
        returndateField.setEditable(false);
        returndateField.setText(returningdate);

        TextField authorField = new TextField();
        authorField.setEditable(false);
        authorField.setText(book.getAuthor());

        TextField titleField = new TextField();
        titleField.setEditable(false);
        titleField.setText(book.getTitle());

        TextField publisherField = new TextField();
        publisherField.setEditable(false);
        publisherField.setText(book.getPublisher());

        TextField isbnField = new TextField();
        isbnField.setEditable(false);
        isbnField.setText(book.getIsbn());

        TextField yearField = new TextField();
        yearField.setEditable(false);
        yearField.setText(book.getYear());

        TextField categoryField = new TextField();
        categoryField.setEditable(false);
        categoryField.setText(book.getCategory());

        TextField copiesField = new TextField();
        copiesField.setEditable(false);
        copiesField.setText(book.getCopies().toString());

        TextField ratingField = new TextField();
        ratingField.setEditable(false);
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted_rating = df.format(book.getRating());
        ratingField.setText(formatted_rating);

        TextField usersField = new TextField();
        usersField.setEditable(false);
        usersField.setText(String.valueOf(book.getVoters()));

        Button comments = new Button("View comments");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Borrowing Date:"), 0, 1);
        grid.add(borrowdateField, 1, 1);
        grid.add(new Label("Returning Date:"), 0, 2);
        grid.add(returndateField, 1, 2);

        grid.add(new Label(""), 0, 3);

        grid.add(new Label("Title:"), 0, 4);
        grid.add(titleField, 1, 4);

        grid.add(new Label("Author:"), 0, 5);
        grid.add(authorField, 1, 5);

        grid.add(new Label("Publisher:"), 0, 6);
        grid.add(publisherField, 1, 6);

        grid.add(new Label("ISBN:"), 0, 7);
        grid.add(isbnField, 1, 7);

        grid.add(new Label("Publication year:"), 0, 8);
        grid.add(yearField, 1, 8);

        grid.add(new Label("Category:"), 0, 9);
        grid.add(categoryField, 1, 9);

        grid.add(new Label("Number of Copies:"), 0, 10);
        grid.add(copiesField, 1, 10);

        grid.add(new Label("Rating:"), 0, 11);
        grid.add(ratingField, 1, 11);
        grid.add(new Label("Number of users who rated:"), 0, 12);
        grid.add(usersField, 1, 12);
        grid.add(new Label("Comments:"), 0, 13);
        grid.add(comments, 1, 13);
        Label label = new Label("");
        grid.add(label, 0, 14);

        ButtonType terminatebtn = new ButtonType("Terminate Loan");
        dialog.getDialogPane().getButtonTypes().add(terminatebtn);

        dialog.getDialogPane().setContent(grid);

        comments.setOnAction(event -> {
            // Create a dialog to display comments
            Dialog<String> commentsDialog = new Dialog<>();
            commentsDialog.setTitle("Comments");
            commentsDialog.setHeaderText("View Comments");

            // Create a text area to display comments
            TextArea commentsTextArea = new TextArea();
            commentsTextArea.setEditable(false);
            commentsTextArea.setWrapText(true);

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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == terminatebtn) {
                // terminate loan
                user.returnBook(book.getIsbn()); // remove book from user's borrowed books (User object)
                book.setCopies(book.getCopies() + 1);
                book.returnBook(user);  // remove BorrowingInfo (user, date) from Book object
                return "terminated";
            }
            return null;
        });

        Optional<String> rslt = dialog.showAndWait();
        if (rslt.isPresent() ) {
            this.result = rslt.get();
        }
        else this.result = null;

    }
}


