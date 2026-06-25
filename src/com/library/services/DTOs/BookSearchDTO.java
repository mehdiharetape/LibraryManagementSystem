package com.library.services.DTOs;

public class BookSearchDTO {
    private int bookId;
    private String isbn;
    private String title;
    private double price;
    private int totaQuality;

    public BookSearchDTO(int bookId,String isbn, String title, double price, int totaQuality)
    {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.totaQuality = totaQuality;
    }

    public int getBookId(){return bookId;}

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getTotaQuality(){return totaQuality;}
}
