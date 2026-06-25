package com.library.conroller;

import com.library.domain.entity.CategoryEntity;
import com.library.mappers.CategoryMapper;
import com.library.services.DTOs.CategoryDTO;
import com.library.services.CategoryService;

import java.util.List;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    //add category
    public boolean handleAddCategory(CategoryDTO category){
        return categoryService.registerNewCategory(CategoryMapper.toEntity(category));
    }

    //get all categories
    public List<CategoryDTO> handleCategoryList(){
        return categoryService.getAllCategories();
    }

    //remove category
    public boolean handleRemoveCategory(int id){
        return categoryService.removeCategory(id);
    }

    //update category
    public boolean handleUpdateCategory(CategoryDTO category){
        var categoryToUpdate = CategoryMapper.toEntity(category);
        return categoryService.updateCategory(categoryToUpdate);
    }

    public CategoryDTO handleGetCategoryById(int categoryId){
        return categoryService.getCategoryByIdService(categoryId);
    }
}
