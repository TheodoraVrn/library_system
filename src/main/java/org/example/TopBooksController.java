package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class TopBooksController {
    @FXML
    private ListView<String> listView;

    @FXML
    void initialize() {
        if (App.books != null) {
            Collections.sort(App.books);
            // Get the top 5 rated books
            List<Book> topRatedBooks = App.books.subList(0, Math.min(5, App.books.size()));
            showListView(topRatedBooks);  // display top 5 rated books to the listview
        }
    }

    private void showListView(List<Book> books) {
        ObservableList<String> bookInfoList = FXCollections.observableArrayList();
        for (Book book : books) {
            // just to make rating prettier for user --> 2 digits accuracy
            DecimalFormat df = new DecimalFormat("#.##");
            String formated_rating = df.format(book.getRating());
            String bookInfo =  "Rating: " + formated_rating + ", " + "Voters: " + book.getVoters() +
                    ", " + "Title: " + book.getTitle() + ", " + "Author: " + book.getAuthor() + ", " +
                    "ISBN: " + book.getIsbn();
            bookInfoList.add(bookInfo);
        }
        listView.setItems(bookInfoList);
    }
}