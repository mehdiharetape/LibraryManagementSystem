package com.library.domain.exception;

public class InvalidEmailException extends LibraryException{
    public InvalidEmailException(){
        super("Email is Invalid.");
    }
}
