package com.library.domain.exception;

public class AuthorNotFoundException extends LibraryException{
    public AuthorNotFoundException(){
        super("Author Not Found.(or authors is empty)");
    }
}
