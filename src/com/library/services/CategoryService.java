package com.library.services;

import com.library.domain.entity.CategoryEntity;
import com.library.domain.exception.CategoryNotFoundException;
import com.library.repository.jdbc.CategoryRepository;
import com.library.services.DTOs.CategoryDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    //add new category
    public boolean registerNewCategory(CategoryEntity category){
        return categoryRepository.create(category);
    }

    //get all categories
    public List<CategoryDTO> getAllCategories(){
        if(categoryRepository.retrieve().isEmpty())
            return new ArrayList<>();
        return categoryRepository.retrieve();
    }

    //remove category
    public boolean removeCategory(int id){
        //check for validate
        if(id < 0 || categoryRepository.getCategoryById(id) == null)
            throw new CategoryNotFoundException();
        return categoryRepository.remove(id);
    }

    //update category
    public boolean updateCategory(CategoryEntity category){
        CategoryDTO founded = categoryRepository.getCategoryById(category.getCategoryId());
        if(category.getCategoryId() <= 0 || founded == null)
            throw new CategoryNotFoundException();
        return categoryRepository.update(category);
    }

    //get category by id
    public CategoryDTO getCategoryByIdService(int categoryId){
        CategoryDTO founded = categoryRepository.getCategoryById(categoryId);
        if(categoryId <= 0 || founded == null)
            throw new CategoryNotFoundException();
        return founded;
    }
}
