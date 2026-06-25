package com.library.domain.exception;

public class InvalidPhoneException extends LibraryException{
    public InvalidPhoneException(){
        super("Phone number is invalid.");
    }
}
