package com.library.conroller;

import com.library.domain.entity.PublisherEntity;
import com.library.mappers.PublisherMapper;
import com.library.services.DTOs.PublisherDTO;
import com.library.services.PublisherService;

import java.util.List;

public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    //add publisher
    public boolean handleAddPublisher(PublisherDTO publisher){
        return publisherService.registerNewPublisher(
                PublisherMapper.toEntity(publisher));
    }

    //get all publishers
    public List<PublisherDTO> handlePublisherList(){
        List<PublisherDTO> publishers = publisherService.getAllPublishers();
        return publishers;
    }

    //remove publisher
    public boolean handlePublisherRemove(int id){
        return publisherService.removePublisher(id);
    }

    //update publisher
    public boolean handlePublisherUpdate(PublisherDTO publisher){
        var publisherToUpdate = PublisherMapper.toEntity(publisher);
        return publisherService.updatePublisher(publisherToUpdate);
    }

    //get publisher by id
    public PublisherDTO handleGetPublisherById(int id){
        return publisherService.getPublisherById(id);
    }
}
