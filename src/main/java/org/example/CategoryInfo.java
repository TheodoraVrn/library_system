package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class CategoryInfo implements Serializable {
    private String title;
    private List<Book> books;

    public CategoryInfo(String title) {
        this.title = title;
        this.books = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    } // add book to the category
    public void removeBook(Book book) {
        books.remove(book);
    } // remove book from the category

}
