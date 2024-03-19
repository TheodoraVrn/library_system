package org.example;

import java.io.Serializable;
import java.time.LocalDate;

class BorrowingInfo implements Serializable {
    private String username;
    private LocalDate borrowDate;

    public BorrowingInfo(String username, LocalDate borrowDate) {
        this.username = username;
        this.borrowDate = borrowDate;
    }

    public String getUser() {
        return username;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return borrowDate.plusDays(5);
    }
}
