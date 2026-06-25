package com.library.services;

import com.library.domain.exception.ValidationException;
import com.library.repository.jdbc.SearchBookRepository;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.BookSearchDTO;

import java.util.List;

public class SearchBookService {
    private final SearchBookRepository searchBookRepository;

    public SearchBookService(SearchBookRepository searchBookRepository){
        this.searchBookRepository = searchBookRepository;
    }

    public List<BookSearchDTO> searchBookService(String keyWord){
        if(keyWord == null || keyWord.isEmpty() || keyWord.length() < 3) {
            throw new ValidationException("less than 3 word!");
        }
        return searchBookRepository.searchBook(keyWord);
    }
}
