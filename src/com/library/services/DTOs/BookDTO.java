package com.library.services.DTOs;

import com.library.domain.entity.Model;
import com.library.domain.valueobject.ISBN;

import java.util.List;

//just for show to user
public class BookDTO implements Model {
    private Integer bookID;
    private String isbn;
    private String title;
    private double price;
    private int publisher_id;
    private int category_id;
    private int totaQuality;
    private List<Integer> authorIds;
    private String bookStatus;

    public BookDTO(Integer bookID ,String isbn, String title, double price, int publisher_id,
                   int category_id, int totaQuality,List<Integer> authorIds ,String bookStatus)
    {
        this.bookID = bookID;
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.publisher_id = publisher_id;
        this.category_id = category_id;
        this.totaQuality = totaQuality;
        this.authorIds = authorIds;
        this.bookStatus = bookStatus;
    }

    public BookDTO(String isbn, String title, double price, int publisher_id,
                   int category_id, int totaQuality, List<Integer> authorIds,String bookStatus)
    {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.publisher_id = publisher_id;
        this.category_id = category_id;
        this.totaQuality = totaQuality;
        this.authorIds = authorIds;
        this.bookStatus = bookStatus;
    }


    //getters
    public Integer getBookID(){return bookID;}

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getTotaQuality(){return totaQuality;}

    public List<Integer> getAuthorIds(){return authorIds;}

    public String getBookStatus(){return bookStatus;}
    //setters

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setTotaQuality(int totaQuality) {
        this.totaQuality = totaQuality;
    }

    public void setAuthorIds(List<Integer> authorIds) {
        this.authorIds = authorIds;
    }

    public void setBookStatus(String bookStatus){
        this.bookStatus = bookStatus;
    }
}
