package com.library.mappers;

import com.library.domain.entity.BookEntity;
import com.library.domain.enums.BookStatus;
import com.library.domain.valueobject.ISBN;
import com.library.services.DTOs.BookDTO;

public class BookMapper {
    //dto to entity
    public static BookEntity toEntity(BookDTO dto){
        BookEntity entity = new BookEntity();
        entity.setBookId(dto.getBookID());
        entity.setIsbn(new ISBN(dto.getIsbn()));
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setPublisher_id(dto.getPublisher_id());
        entity.setCategory_id(dto.getCategory_id());
        entity.setTotalQuantity(dto.getTotaQuality());
        entity.setAuthorIds(dto.getAuthorIds());
        entity.setBookStatus(BookStatus.valueOf(dto.getBookStatus()));

        return entity;
    }
}
