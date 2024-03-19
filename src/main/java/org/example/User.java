package org.example;

import java.io.Serializable;
import java.util.List;

class User implements Serializable {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String adt;
    private String email;
    private int numberofbooks;
    public List<String> isbns; // stores the isbns of the books that user has borrowed (open loans)

    public User(String username, String password, String firstname, String lastname, String adt,
                String email, Integer numberofbooks, List<String> isbns) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.adt = adt;
        this.email = email;
        this.numberofbooks = numberofbooks;
        this.isbns = isbns;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getAdt() {
        return adt;
    }
    public String getEmail() {
        return email;
    }


    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public void setAdt(String adt){
        this.adt = adt;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public int getNumberofBooks() {
        return numberofbooks;
    }
    public void borrowBook(String isbn) {
        numberofbooks ++;
        isbns.add(isbn);
    }
    public void returnBook(String isbn) {
        numberofbooks --;
        isbns.remove(isbn);
    }
}
