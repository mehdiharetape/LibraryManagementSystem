package com.library.domain.exception;

public class BookNotAvailableException extends LibraryException{
    public BookNotAvailableException(){
        super("Book is not available.");
    }
}
