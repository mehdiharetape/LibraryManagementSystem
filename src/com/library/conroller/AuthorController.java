package com.library.conroller;

import com.library.domain.entity.AuthorEntity;
import com.library.mappers.AuthorMapper;
import com.library.services.AuthorService;
import com.library.services.DTOs.AuthorDTO;

import java.util.List;

public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    //create new author
    public boolean handleCreateAuthor(AuthorDTO author){
        var authorToAdd = AuthorMapper.toEntity(author);
        return authorService.registerNewAuthor(authorToAdd);
    }

    //get all authors
    public List<AuthorDTO> handleAuthorList(){
        return authorService.getAllAuthors();
    }

    //remove author
    public boolean handleRemoveAuthor(int id){
        return authorService.removeAuthor(id);
    }

    //update author
    public boolean handleUpdateAuthor(AuthorDTO author){
        var authorToUpdate = AuthorMapper.toEntity(author);
        return authorService.updateAuthor(authorToUpdate);
    }

    //get author by Id
    public AuthorDTO handleGetAuthorById(int id){
        return authorService.getAuthorByIdService(id);
    }
}
