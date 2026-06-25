package com.library.domain.exception;

public class InvalidIsbnException extends LibraryException{
    public InvalidIsbnException(){
        super("ISBN is invalid.");
    }
}
