package com.library.domain.exception;

public class LoanLimitException extends LibraryException{
    public LoanLimitException(){
        super("Loan for this member is Limited.");
    }
}
