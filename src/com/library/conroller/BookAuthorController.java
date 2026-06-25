package com.library.conroller;

import com.library.domain.entity.BookAuthorEntity;
import com.library.services.BookAuthorService;
import com.library.services.DTOs.BookAuthorDTO;

import java.util.List;

public class BookAuthorController {
    private final BookAuthorService bookAuthorService;

    public BookAuthorController(BookAuthorService bookAuthorService){
        this.bookAuthorService = bookAuthorService;
    }

    //create
    public void handleAddBookAuthor(int authorId, int bookId){
        BookAuthorEntity bookAuthor = new BookAuthorEntity(authorId, bookId);
        boolean isCreated = bookAuthorService.addBookAuthor(bookAuthor);

        if(isCreated)
            System.out.println("Controller : book-author added successfully");
        else
            System.out.println("Controller : failed");
    }

    //get all
    public List<BookAuthorDTO> handleBookAuthorList(){
        return bookAuthorService.getAllBookAuthors();
    }

    //remove
    public void handleRemoveBookAuthor(int id){
        boolean isSuccess = bookAuthorService.removeBookAuthor(id);

        if(isSuccess)
            System.out.println("Controller : Successful");
        else
            System.out.println("Controller : failed");
    }

    //update
    public void handleBookAuthorUpdate(int id, int authorId, int bookId){
        boolean isUpdated = bookAuthorService.updateBookAuthor(id, authorId, bookId);

        if(isUpdated)
            System.out.println("Controller : book-author updated successfully");
        else
            System.out.println("Controller : failed");
    }
}
