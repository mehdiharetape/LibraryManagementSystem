package com.library.mappers;

import com.library.domain.entity.PublisherEntity;
import com.library.services.DTOs.PublisherDTO;

public class PublisherMapper {
    public static PublisherEntity toEntity(PublisherDTO dto){
        PublisherEntity entity = new PublisherEntity();
        entity.setPublisherId(dto.getPublisherId());
        entity.setPublisherName(dto.getPublisherName());
        entity.setPublisherUrl(dto.getPublisherUrl());
        return entity;
    }
}
