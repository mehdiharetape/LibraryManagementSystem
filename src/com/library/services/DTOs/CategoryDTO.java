package com.library.services.DTOs;

import com.library.domain.entity.Model;

public class CategoryDTO implements Model {
    private Integer categoryId;
    private String categoryName;

    public CategoryDTO(Integer categoryId ,String categoryName){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }


    public Integer getCategoryId(){return categoryId;}
    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public String toString(){
        return this.getCategoryName();
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        CategoryDTO category = (CategoryDTO) o;
        return this.categoryId == category.getCategoryId();
    }
}
