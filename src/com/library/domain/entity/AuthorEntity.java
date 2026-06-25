package com.library.domain.entity;

import com.library.domain.exception.AuthorNotFoundException;
import com.library.domain.exception.ValidationException;

public class AuthorEntity implements Model {
    private Integer authorId;
    private String authorName;
    private String authorUrl;

    public AuthorEntity(){}

    public AuthorEntity(Integer authorId ,String authorName, String authorUrl){
        if(authorId != null && authorId < 2000)
            throw new AuthorNotFoundException();
        this.authorId = authorId;
        if (authorName.length() < 5)
            throw new ValidationException("Author name can't be less than 5 characters");
        this.authorName = authorName;
        this.authorUrl = authorUrl;
    }

    public Integer getAuthorId(){
        return authorId;
    }

    public String getAuthorName(){
        return authorName;
    }

    public String getAuthorUrl(){
        return authorUrl;
    }

    //setters
    public void setAuthorId(Integer authorId){
        if(authorId != null && authorId < 2000)
            throw new AuthorNotFoundException();
        this.authorId = authorId;
    }

    public void setAuthorName(String authorName) {
        if (authorName.length() < 5)
            throw new ValidationException("Author name can't be less than 5 characters");
        this.authorName = authorName;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }
}
