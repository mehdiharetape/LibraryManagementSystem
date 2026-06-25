package com.library.services.DTOs;

public class BookListMiniDTO {
    private int bookId;
    private String title;
    private String isbn;
    private int totalQuantity;

    public BookListMiniDTO(int bookId, String title, String isbn, int totalQuantity){
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.totalQuantity = totalQuantity;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }
}
