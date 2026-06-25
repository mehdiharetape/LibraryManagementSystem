package com.library.domain.entity;

import com.library.domain.enums.BookStatus;
import com.library.domain.exception.*;
import com.library.domain.valueobject.ISBN;

import java.util.List;

public class BookEntity implements Model {
    private Integer bookId;
    private ISBN isbn;
    private String title;
    private double price;
    private int publisher_id;
    private int category_id;
    private int totalQuantity;
    private List<Integer> authorIds;
    private BookStatus bookStatus;

    public BookEntity(){}

    public BookEntity(Integer bookId, ISBN isbn,String title, double price, int publisher_id,
                      int category_id, int totalQuantity, List<Integer> authorIds,
                      BookStatus bookStatus)
    {
        if (bookId != null && bookId < 300)
            throw new IllegalArgumentException("Invalid Book id!!!");
        this.bookId = bookId;
        validation(isbn ,title, price, publisher_id, category_id, totalQuantity, authorIds, bookStatus);
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.publisher_id = publisher_id;
        this.category_id = category_id;
        this.totalQuantity = totalQuantity;
        this.authorIds = authorIds;
        this.bookStatus = bookStatus;
    }

    //---------getters---------------
    public Integer getBookId(){
        return this.bookId;
    }

    public List<Integer> getAuthorIds(){
        return authorIds;
    }

    public ISBN getIsbn() {
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

    public int getTotalQuantity(){return totalQuantity;}

    public BookStatus getBookStatus(){return bookStatus;}

    //----------------setters-----------------------

    public void setBookId(Integer bookId){
        if (bookId != null && bookId < 300)
            throw new IllegalArgumentException("Invalid Book id!!!");
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        if(title.length() < 5)
            throw new ValidationException("Title can't be less than 5 characters!!!");
        this.title = title;
    }

    public void setIsbn(ISBN isbn) {
        if(isbn == null)
            throw new InvalidIsbnException();
        this.isbn = isbn;
    }

    public void setPrice(double price) {
        if(price <= 0)
            throw new ValidationException("Invalid Price !!!");
        this.price = price;
    }

    public void setPublisher_id(int publisher_id) {
        if(publisher_id < 1000)
            throw new PublisherNotFoundException();
        this.publisher_id = publisher_id;
    }

    public void setCategory_id(int category_id) {
        if(category_id <= 0)
            throw new CategoryNotFoundException();
        this.category_id = category_id;
    }

    public void setTotalQuantity(int totalQuantity) {
        if (totalQuantity < 0)
            throw new ValidationException("Invalid Quantity!!!");
        this.totalQuantity = totalQuantity;
    }

    public void setAuthorIds(List<Integer> authorIds) {
        if (authorIds == null)
            throw new AuthorNotFoundException();
        this.authorIds = authorIds;
    }

    public void setBookStatus(BookStatus bookStatus){
        if(bookStatus == null)
            throw new ValidationException("Invalid Book Status!!!");
        this.bookStatus = bookStatus;
    }

    //validation check
    private void validation(ISBN isbn, String title, double price, int publisher_id,
                            int category_id, int totalQuantity, List<Integer> authorIds,
                            BookStatus bookStatus)
    {
        if(isbn == null)
            throw new InvalidIsbnException();
        if(title.length() < 5)
            throw new ValidationException("Title can't be less than 5 characters!!!");
        if(price <= 0)
            throw new ValidationException("Invalid Price !!!");
        if(publisher_id < 1000)
            throw new PublisherNotFoundException();
        if(category_id <= 0)
            throw new CategoryNotFoundException();
        if (totalQuantity < 0)
            throw new ValidationException("Invalid Quantity!!!");
        if (authorIds == null)
            throw new AuthorNotFoundException();
        if(bookStatus == null)
            throw new ValidationException("Invalid Book Status!!!");
    }
}

