package com.library.services;

import com.library.domain.entity.AuthorEntity;
import com.library.domain.exception.AuthorNotFoundException;
import com.library.repository.jdbc.AuthorRepository;
import com.library.services.DTOs.AuthorDTO;

import java.util.ArrayList;
import java.util.List;

public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    //create new author
    public boolean registerNewAuthor(AuthorEntity author){
        return authorRepository.create(author);
    }

    //get all authors
    public List<AuthorDTO> getAllAuthors(){
        if(authorRepository.retrieve().isEmpty()){
            return new ArrayList<>();
        }
        return authorRepository.retrieve();
    }

    //remove author
    public boolean removeAuthor(int id){
        AuthorDTO founded = authorRepository.findAuthorById(id);
        if(founded == null)
            throw new AuthorNotFoundException();
        return authorRepository.remove(id);
    }

    //update author
    public boolean updateAuthor(AuthorEntity author)
    {
        AuthorDTO founded = authorRepository.findAuthorById(author.getAuthorId());
        if(founded == null)
            throw new AuthorNotFoundException();
        return authorRepository.update(author);
    }

    //get author by id
    public AuthorDTO getAuthorByIdService(int authorId){
        AuthorDTO founded = authorRepository.findAuthorById(authorId);
        if(founded == null)
            throw new AuthorNotFoundException();
        return founded;
    }
}
