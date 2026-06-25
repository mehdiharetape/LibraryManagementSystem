package com.library.services;

import com.library.domain.entity.BookAuthorEntity;
import com.library.repository.jdbc.BookAuthorRepository;
import com.library.services.DTOs.BookAuthorDTO;

import java.util.List;

public class BookAuthorService {
    private final BookAuthorRepository bookAuthorRepository;

    public BookAuthorService(BookAuthorRepository bookAuthorRepository){
        this.bookAuthorRepository = bookAuthorRepository;
    }

    //create
    public boolean addBookAuthor(BookAuthorEntity bookAuthor){
        if(bookAuthor.getAuthorId() < 2000){
            System.out.println("Service : Invalid author id!");
            return false;
        }
        else if(bookAuthor.getBookIk() < 300){
            System.out.println("Service : Invalid book id!");
            return false;
        }
        System.out.println("Service : add book-author successfully");
        return bookAuthorRepository.create(bookAuthor);
    }

    //get all book-authors
    public List<BookAuthorDTO> getAllBookAuthors(){
        List<BookAuthorDTO> bookAuthors = bookAuthorRepository.retrieve();
        if(bookAuthors.isEmpty()){
            System.out.println("Service : there is no book-authors");
        }
        return bookAuthors;
    }

    //remove
    public boolean removeBookAuthor(int id){
        if(id <= 0){
            System.out.println("Service : id can't be empty");
            return false;
        }
        return bookAuthorRepository.remove(id);
    }

    //update
    public boolean updateBookAuthor(int id, int authorId, int bookId){
        if(id <= 0){
            System.out.println("Service : invalid id");
            return false;
        }
        else if(authorId < 2000){
            System.out.println("Service : invalid author id");
            return false;
        }
        else if(bookId < 300){
            System.out.println("Service : invalid book id");
            return false;
        }
        System.out.println("updated successfully");
        return bookAuthorRepository.update(id, authorId, bookId);
    }
}
