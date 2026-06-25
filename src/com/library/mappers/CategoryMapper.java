package com.library.mappers;

import com.library.domain.entity.CategoryEntity;
import com.library.services.DTOs.CategoryDTO;

public class CategoryMapper {
    public static CategoryEntity toEntity(CategoryDTO dto){
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryId(dto.getCategoryId());
        entity.setCategoryName(dto.getCategoryName());
        return entity;
    }
}
