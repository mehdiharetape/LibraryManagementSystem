package com.library.services.DTOs;

public class BorrowedBook {
    private String bookTitle;
    private String isbn;
    private String fromDate;
    private String toDate;

    public BorrowedBook(String bookTitle,String isbn,String fromDate, String toDate){
        this.bookTitle = bookTitle;
        this.isbn = isbn;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getIsbn(){
        return isbn;
    }


    @Override
    public String toString(){
        return String.format("Book : %s || isbn : %s || loan date : %s || expire date : %s ---",
                bookTitle,isbn ,fromDate, toDate);
    }
}
