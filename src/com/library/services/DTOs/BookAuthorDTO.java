package com.library.services.DTOs;

public class BookAuthorDTO {
    private int bookAuthorId;
    private String authorName;
    private String bookName;

    public BookAuthorDTO(int bookAuthorId ,String authorName, String bookName){
        this.bookAuthorId = bookAuthorId;
        this.authorName = authorName;
        this.bookName = bookName;
    }

    public int getBookAuthorId(){return bookAuthorId;}
    public String getAuthorName(){return authorName;}
    public String getBookName(){return bookName;}
}
