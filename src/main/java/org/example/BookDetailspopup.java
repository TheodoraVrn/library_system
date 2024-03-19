package org.example;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;

public class BookDetailspopup {
    public BookDetailspopup(Book book, String username) {
        // Create a dialog to display book's details
        Dialog<String> dialog = new Dialog<>();
        dialog.setHeaderText("Book Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20,150,10,10));

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

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);

        grid.add(new Label("Publisher:"), 0, 2);
        grid.add(publisherField, 1, 2);

        grid.add(new Label("ISBN:"), 0, 3);
        grid.add(isbnField, 1, 3);

        grid.add(new Label("Publication year:"), 0, 4);
        grid.add(yearField, 1, 4);

        grid.add(new Label("Category:"), 0, 5);
        grid.add(categoryField, 1, 5);

        grid.add(new Label("Number of Copies:"), 0, 6);
        grid.add(copiesField, 1, 6);

        grid.add(new Label("Rating:"), 0, 7);
        grid.add(ratingField, 1, 7);
        grid.add(new Label("Number of users who rated:"), 0, 8);
        grid.add(usersField, 1, 8);
        grid.add(new Label("Comments:"), 0, 9);
        grid.add(comments, 1, 9);
        Label label = new Label("");
        grid.add(label, 0, 10);

        int copiesvalue = Integer.parseInt(copiesField.getText());
        ButtonType borrow = new ButtonType("Borrow");
        ButtonType exit = new ButtonType("Exit");

        if (copiesvalue == 0) {
            dialog.getDialogPane().getButtonTypes().add(exit);
            label.setTextFill(Color.RED);
            label.setText("*Sorry, no available copies.");
        }
        else { // available copies --> user can borrow book
            dialog.getDialogPane().getButtonTypes().add(borrow);
        }

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
            if (dialogButton == borrow) { // user clicked the borrow button
                if (copiesvalue > 0) {
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
                else {
                    label.setText("*Sorry, no availabale copies.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}

