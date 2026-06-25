package com.library.domain.exception;

public class PublisherNotFoundException extends LibraryException{
    public PublisherNotFoundException(){
        super("Publisher Not Found");
    }
}
