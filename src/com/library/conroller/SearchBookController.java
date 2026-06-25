package com.library.conroller;

import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.BookSearchDTO;
import com.library.services.SearchBookService;

import java.util.List;

public class SearchBookController {
    private final SearchBookService searchBookService;

    public SearchBookController(SearchBookService searchBookService){
        this.searchBookService = searchBookService;
    }

    public List<BookSearchDTO> handleSearchBook(String keyWord){
        return searchBookService.searchBookService(keyWord);
    }
}
