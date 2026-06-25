package com.library.conroller;

import com.library.domain.enums.BookStatus;
import com.library.domain.valueobject.ISBN;
import com.library.mappers.BookMapper;
import com.library.services.DTOs.BookDTO;
import com.library.domain.entity.BookEntity;
import com.library.services.DTOs.BookListMiniDTO;
import com.library.services.BookService;
import com.library.services.DTOs.BooksListDTO;

import java.util.List;

public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    //add book
    public boolean handleAddBook(BookDTO bookDto)
    {
        BookEntity book = BookMapper.toEntity(bookDto);
        return bookService.registerNewBook(book);
    }

    //get all books
    public List<BooksListDTO> handleBookList(){
        List<BooksListDTO> books = bookService.getAllBooks();
        return books;
    }

    //remove books
    public boolean handleRemoveBook(int id){
        return bookService.removeBook(id);
    }

    //update books
    public boolean handleUpdateBook(BookDTO book){
        BookEntity bookToUpdate = BookMapper.toEntity(book);
        boolean isUpdate = bookService.updateBook(bookToUpdate);
        if (isUpdate)
            return true;
        else
            return false;
    }

    //get all books of a publisher
    public List<BookListMiniDTO> handleGetAllPublisherBook(int publisherId){
        return bookService.getPublisherBookService(publisherId);
    }

    //get all books of a category
    public List<BookListMiniDTO> handleGetAllCategoryBook(int categoryId){
        return bookService.getCategoryBookService(categoryId);
    }

    //get all books of an author
    public List<BookListMiniDTO> handleGetAllBooksOfAuthor(int authorId){
        return bookService.getAllBooksOfAuthor(authorId);
    }

    //get book by id
    public BookDTO handleGetBookById(int id){
        return bookService.getBookById(id);
    }

    //get all authors of a book
    public List<String> handleGetAllAuthorsOfBook(int bookId){
        return bookService.getAllAuthorOfBookService(bookId);
    }
}
