package com.library.domain.exception;

public class LoanNotFoundException extends LibraryException{
    public LoanNotFoundException(){
        super("Loan Not Found.");
    }
}
