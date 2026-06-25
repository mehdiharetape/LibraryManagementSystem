package com.library.domain.valueobject;

import com.library.domain.exception.InvalidIsbnException;

public class ISBN {
    private final String value;

    public ISBN(String value){
        String normalized = value.replaceAll("-", "").trim();
        if(isValidIsbn(normalized))
            this.value = value;
        else
            throw new InvalidIsbnException();
    }

    public String getValue(){
        return value;
    }

    public static boolean isValidIsbn(String isbn){
        String normalized = isbn.replaceAll("-", "").trim();
        return isValidISBN10(normalized) || isValidISBN13(normalized);
    }

    //----ISBN 10-----------
    private static boolean isValidISBN10(String isbn){
        if(isbn == null || !isbn.matches("\\d{9}[\\dX]"))
            return false;
        int sum = 0;
        for(int i = 0; i < 9; i++)
            sum += (isbn.charAt(i) - '0') * (10 - i);

        char last = isbn.charAt(0);
        sum += (last == 'X') ? 10 : (last - '0');
        return sum % 11 == 0;
    }


    //----ISBN 13-----------
    private static boolean isValidISBN13(String isbn){
        if(isbn == null || !isbn.matches("\\d{13}"))
            return false;
        int sum = 0;
        for(int i = 0; i < 12; i++){
            int digit = isbn.charAt(i) - '0';
            if(i % 2 == 0) sum += digit;
            else sum += digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return checkDigit == (isbn.charAt(12) - '0');
    }
}
