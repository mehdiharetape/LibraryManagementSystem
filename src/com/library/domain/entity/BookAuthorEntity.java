package com.library.domain.entity;

public class BookAuthorEntity {
    private int authorId;
    private int bookIk;

    public BookAuthorEntity(int authorId, int bookIk){
        this.authorId = authorId;
        this.bookIk = bookIk;
    }

    public int getAuthorId(){return authorId;}
    public int getBookIk(){return bookIk;}
}
