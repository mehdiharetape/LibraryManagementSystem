package com.library.domain.exception;

public class DuplicateAdminUserNameException extends LibraryException{
    public DuplicateAdminUserNameException(){
        super("This User name already exists.");
    }
}
