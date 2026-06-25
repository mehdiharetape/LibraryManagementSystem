package com.library.services.DTOs;

public class AuthorDTO {
    private Integer authorId;
    private String authorName;
    private String authorUrl;

    public AuthorDTO(Integer authorId, String authorName, String authorUrl){
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String toString(){
        return this.getAuthorName();
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        AuthorDTO author = (AuthorDTO) o;
        return this.authorId == author.getAuthorId();
    }
}
