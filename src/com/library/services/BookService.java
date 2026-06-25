package com.library.services;

import com.library.domain.entity.BookEntity;
import com.library.domain.exception.AuthorNotFoundException;
import com.library.domain.exception.BookNotFoundException;
import com.library.domain.exception.CategoryNotFoundException;
import com.library.domain.exception.PublisherNotFoundException;
import com.library.repository.jdbc.AuthorRepository;
import com.library.repository.jdbc.BookRepository;
import com.library.repository.jdbc.CategoryRepository;
import com.library.repository.jdbc.PublisherRepository;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.BookListMiniDTO;
import com.library.services.DTOs.BooksListDTO;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository,
                       CategoryRepository categoryRepository, AuthorRepository authorRepository){
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
    }

    //add book
    public boolean registerNewBook(BookEntity book){
        //check publisher exist
        if(publisherRepository.findPublisherById(book.getPublisher_id()) == null)
            throw new PublisherNotFoundException();
        //check category exist
        if(categoryRepository.getCategoryById(book.getCategory_id()) == null)
            throw new CategoryNotFoundException();
        //check author ids
        for(Integer id : book.getAuthorIds()){
            if(authorRepository.findAuthorById(id) == null)
                throw new AuthorNotFoundException();
        }
        return bookRepository.create(book);
    }

    //get all books
    public List<BooksListDTO> getAllBooks(){
        if(bookRepository.retrieve().isEmpty())
            return new ArrayList<>();
        return bookRepository.retrieve();
    }

    //remove book
    public boolean removeBook(int id){
        //منطق بیشتر در آینده
        if(id < 300 || bookRepository.getBookById(id) == null)
            throw new BookNotFoundException();
        return bookRepository.remove(id);
    }

    //update book
    public boolean updateBook(BookEntity book)
    {
        //check publisher exist
        if(publisherRepository.findPublisherById(book.getPublisher_id()) == null)
            throw new PublisherNotFoundException();
        //check category exist
        if(categoryRepository.getCategoryById(book.getCategory_id()) == null)
            throw new CategoryNotFoundException();
        //check author ids
        for(Integer id : book.getAuthorIds()){
            if(authorRepository.findAuthorById(id) == null)
                throw new AuthorNotFoundException();
        }
        return bookRepository.update(book);
    }

    //get all books of a publisher
    public List<BookListMiniDTO> getPublisherBookService(int publisherId){
        if(publisherId < 1000 || publisherRepository.findPublisherById(publisherId) == null)
            throw new PublisherNotFoundException();
        return bookRepository.getPublisherBookList(publisherId);
    }

    //get all books of a publisher
    public List<BookListMiniDTO> getCategoryBookService(int categoryId){
        if(categoryId < 0 || !categoryRepository.isCategoryExists(categoryId))
            throw new CategoryNotFoundException();
        return bookRepository.getCategoryBookList(categoryId);
    }

    //get all books of an author
    public List<BookListMiniDTO> getAllBooksOfAuthor(int authorId){
        if(authorId < 2000 || authorRepository.findAuthorById(authorId) == null)
            throw new AuthorNotFoundException();
        return bookRepository.getAllBooksOfAuthor(authorId);
    }

    //get book with id
    public BookDTO getBookById(int id){
        if(id < 300)
            throw new BookNotFoundException();
        return bookRepository.getBookById(id);
    }

    //get authors of a book
    public List<String> getAllAuthorOfBookService(int bookId){
        if(bookId < 300 || bookRepository.getBookById(bookId) == null)
            throw new BookNotFoundException();
        return bookRepository.getAllAuthorOfBook(bookId);
    }
}
