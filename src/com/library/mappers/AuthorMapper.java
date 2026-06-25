package com.library.mappers;

import com.library.domain.entity.AuthorEntity;
import com.library.services.DTOs.AuthorDTO;

public class AuthorMapper {
    public static AuthorEntity toEntity(AuthorDTO dto){
        AuthorEntity entity = new AuthorEntity();
        entity.setAuthorId(dto.getAuthorId());
        entity.setAuthorName(dto.getAuthorName());
        entity.setAuthorUrl(dto.getAuthorUrl());
        return entity;
    }
}
