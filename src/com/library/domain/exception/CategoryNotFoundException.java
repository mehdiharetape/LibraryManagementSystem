package com.library.domain.exception;

public class CategoryNotFoundException extends LibraryException{
    public CategoryNotFoundException(){
        super("Category Not Found.");
    }
}
