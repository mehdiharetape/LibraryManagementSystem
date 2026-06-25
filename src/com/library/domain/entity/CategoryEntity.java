package com.library.domain.entity;

import com.library.domain.exception.ValidationException;

public class CategoryEntity implements Model {
    private Integer categoryId;
    private String categoryName;

    public CategoryEntity(){}

    public CategoryEntity(Integer categoryId ,String categoryName){
        if(categoryId != null && categoryId < 0)
            throw new ValidationException("Invalid Category Id!!!");
        this.categoryId = categoryId;

        if(categoryName.length() < 5)
            throw new ValidationException("Invalid Category Name. (less than 5 characters)");
        this.categoryName = categoryName;
    }

    public Integer getCategoryId(){
        return categoryId;
    }

    public String getCategoryName(){
        return categoryName;
    }

    //setters

    public void setCategoryId(Integer categoryId) {
        if(categoryId != null && categoryId < 0)
            throw new ValidationException("Invalid Category Id!!!");
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        if(categoryName.length() < 5)
            throw new ValidationException("Invalid Category Name. (less than 5 characters)");
        this.categoryName = categoryName;
    }
}
