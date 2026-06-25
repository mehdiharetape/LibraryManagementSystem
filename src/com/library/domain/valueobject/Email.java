package com.library.domain.valueobject;

import com.library.domain.exception.InvalidEmailException;

public class Email {
    private final String value;

    public Email(String value){
        if(isValidEmail(value))
            this.value = value;
        else
            throw new InvalidEmailException();
    }


    public String getValue(){
        return value;
    }

    public static boolean isValidEmail(String email){
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(email == null || !email.matches(EMAIL_REGEX))
            return false;
        else
            return true;
    }
}
