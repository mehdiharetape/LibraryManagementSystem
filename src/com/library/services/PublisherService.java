package com.library.services;

import com.library.domain.entity.PublisherEntity;
import com.library.domain.exception.PublisherNotFoundException;
import com.library.repository.jdbc.PublisherRepository;
import com.library.services.DTOs.PublisherDTO;

import java.util.ArrayList;
import java.util.List;

public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository){
        this.publisherRepository = publisherRepository;
    }

    //add publisher
    public boolean registerNewPublisher(PublisherEntity publisher){
        return publisherRepository.create(publisher);
    }

    //get all publishers
    public List<PublisherDTO> getAllPublishers(){
        if(publisherRepository.retrieve().isEmpty()) {
            return new ArrayList<>();
        }
        return publisherRepository.retrieve();
    }

    //remove publisher
    public boolean removePublisher(int id){
        PublisherDTO founded = publisherRepository.findPublisherById(id);
        if (founded == null)
            throw new PublisherNotFoundException();
        return publisherRepository.remove(id);
    }

    //update publisher
    public boolean updatePublisher(PublisherEntity publisher){
        return publisherRepository.update(publisher);
    }

    //find publisher by id
    public PublisherDTO getPublisherById(int id){
        PublisherDTO founded = publisherRepository.findPublisherById(id);
        if (founded == null)
            throw new PublisherNotFoundException();
        return founded;
    }
}
