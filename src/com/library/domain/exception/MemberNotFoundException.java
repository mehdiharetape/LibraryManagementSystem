package com.library.domain.exception;

public class MemberNotFoundException extends LibraryException{
    public MemberNotFoundException(){
        super("Member Not Found.");
    }
}
