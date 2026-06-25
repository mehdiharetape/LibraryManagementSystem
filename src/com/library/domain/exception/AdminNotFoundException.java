package com.library.domain.exception;

public class AdminNotFoundException extends LibraryException{
    public AdminNotFoundException(){
        super("Admin not found.");
    }
}
