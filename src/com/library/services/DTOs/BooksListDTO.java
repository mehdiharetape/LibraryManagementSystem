package com.library.services.DTOs;

import com.library.domain.valueobject.ISBN;

public class BooksListDTO {
    private int bookId;
    private String isbn;
    private String title;
    private double price;
    private String publisherName;
    private String categoryName;
    private int totalQuantity;
    private String bookStatus;

    public BooksListDTO(int bookId, String isbn, String title, double price,
                       String publisherName, String categoryName, int totalQuantity,
                        String bookStatus)
    {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.publisherName = publisherName;
        this.categoryName = categoryName;
        this.totalQuantity = totalQuantity;
        this.bookStatus = bookStatus;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public String getBookStatus(){
        return bookStatus;
    }
}
