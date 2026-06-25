package com.library.domain.exception;

public class BookNotFoundException extends LibraryException{
    public BookNotFoundException(){
        super("Book Not Found.");
    }
}
