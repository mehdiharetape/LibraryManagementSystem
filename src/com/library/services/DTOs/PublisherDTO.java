package com.library.services.DTOs;

import com.library.domain.entity.Model;

public class PublisherDTO implements Model {
    private Integer publisherId;
    private String publisherName;
    private String publisherUrl;

    public PublisherDTO(Integer publisherId , String publisherName, String publisherUrl){
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.publisherUrl = publisherUrl;
    }

    public Integer getPublisherId(){
        return publisherId;
    }

    public String getPublisherName(){
        return publisherName;
    }

    public String getPublisherUrl(){
        return publisherUrl;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public void setPublisherUrl(String publisherUrl) {
        this.publisherUrl = publisherUrl;
    }

    public String toString(){
        return this.getPublisherName();
    }
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        PublisherDTO publisher = (PublisherDTO) o;
        return this.publisherId == publisher.getPublisherId();
    }
}
