package com.library.domain.entity;

import com.library.domain.exception.PublisherNotFoundException;
import com.library.domain.exception.ValidationException;

public class PublisherEntity implements Model {
    private Integer publisherId;
    private String publisherName;
    private String publisherUrl;

    public PublisherEntity(){}


    public PublisherEntity(Integer publisherId ,String publisherName, String publisherUrl){
        if(publisherId != null && publisherId < 1000)
            throw new PublisherNotFoundException();
        this.publisherId = publisherId;

        if(publisherName.length() < 5)
            throw new ValidationException("Publisher Name Can't Be less than 5 characters !!!!");
        this.publisherName = publisherName;
        this.publisherUrl = publisherUrl;
    }

    public Integer getPublisherId(){return publisherId;}

    public String getPublisherName(){
        return publisherName;
    }

    public String getPublisherUrl(){
        return publisherUrl;
    }
    //------setters--------
    public void setPublisherId(Integer publisherId){
        if(publisherId != null && publisherId < 1000)
            throw new PublisherNotFoundException();
        this.publisherId = publisherId;
    }

    public void setPublisherName(String publisherName) {
        if(publisherName.length() < 5)
            throw new ValidationException("Publisher Name Can't Be less than 5 characters !!!!");
        this.publisherName = publisherName;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }
}
