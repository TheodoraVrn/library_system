package org.example;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

class Book implements Comparable<org.example.Book>, Serializable {
    @Override
    public int compareTo(org.example.Book other) {
        // Compare books based on their ratings
        return Double.compare(other.rating, this.rating);
    }
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String year;
    private String category;
    private Integer copies;
    private Float rating;
    private Integer voters;
    public Map<String, Float> mapRatings; // Map to store user ratings for the book (Username, Rating)
    private List<String> comments;

    public List<BorrowingInfo> userborrowings; // stores the username and borrowing date for the open loans of the book

    public Book(String title, String author, String publisher, String isbn, String year,
                String category, Integer copies, Integer rating, Integer voters, Map<String, Float> mapRatings,
                List<String> comments, List<BorrowingInfo> userborrowings) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.year = year;
        this.category = category;
        this.copies = copies;
        this.rating = (float) rating;
        this.voters = voters;
        this.mapRatings = mapRatings;
        this.comments = comments;
        this.userborrowings = userborrowings;
    }

    public void addComment(String comment) { // add comment to the list
        comments.add(comment);
    }
    public void borrowBook(User user) {
        // add username and borrowing date to the list
        userborrowings.add(new BorrowingInfo(user.getUsername(), LocalDate.now()));
    }
    public void returnBook(User user) {
        // Use Iterator to safely remove the BorrowingInfo
        Iterator<BorrowingInfo> iterator = userborrowings.iterator();
        while (iterator.hasNext()) {
            BorrowingInfo info = iterator.next();
            if (info.getUser().equals(user.getUsername())) {
                iterator.remove(); // Remove the BorrowingInfo for this user (terminate loan)
                break;
            }
        }
    }

    public void updateBorrowingInfo(LocalDate borrowdate, User user) {
        // add the specified borrowing date for the user who borrowed the book
        userborrowings.add(new BorrowingInfo(user.getUsername(), borrowdate));
    }

    public void updateRate(String username, User newuser) {
        if (mapRatings.containsKey(username)) { // Check if the user has rated the book
            float rating = mapRatings.get(username); // Retrieve rating for the given username
            mapRatings.remove(username, rating); // Remove (user, rating) from the map
            mapRatings.put(newuser.getUsername(), rating); // Add updated (user, rating) to the map
        }
    }

    public Integer rateBook(String username, float rating) {
        if (!mapRatings.containsKey(username)) { // Check if the user has not already rated the book
            mapRatings.put(username, rating); // Add user's rating to the map
            calculateRate(); // Recalculate the book's overall rating
            increaseVoters();
            return 1;
        }
        return 0; // the user has already rated the book
    }
    private void calculateRate() {
        float totalRating = 0.0f;
        for (float userRating : mapRatings.values()) {
            totalRating += userRating;
        }
        this.rating = totalRating / mapRatings.size(); // Calculate the average rating
    }
    public void increaseVoters() {
        voters ++;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setCopies(int copies){
        this.copies = copies;
    }

    // Getters
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getIsbn() {
        return isbn;
    }
    public String getYear() {
        return year;
    }
    public String getCategory() {
        return category;
    }
    public Integer getCopies() {
        return copies;
    }
    public Float getRating() {
        return rating;
    }
    public Integer getVoters() {
        return voters;
    }
    public List<String> getComments() {return comments;}
}
