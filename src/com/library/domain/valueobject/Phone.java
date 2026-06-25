package com.library.domain.valueobject;

import com.library.domain.exception.InvalidPhoneException;

public class Phone {
    private final String value;

    public Phone(String value){
        if(isValidPhone(value))
            this.value = value;
        else
            throw new InvalidPhoneException();
    }

    public String getValue(){
        return value;
    }

    public static boolean isValidPhone(String phone){
        String PHONE_REGEX = "^(\\98|0)?9\\d{9}$";
        if(phone == null || !phone.matches(PHONE_REGEX))
            return false;
        else return true;
    }
}
