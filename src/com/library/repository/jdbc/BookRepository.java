package com.library.repository.jdbc;

import com.library.domain.entity.BookEntity;
import com.library.domain.valueobject.ISBN;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.BookListMiniDTO;
import com.library.infrastructure.persistance.CreateConnection;
import com.library.services.DTOs.BooksListDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final CreateConnection connection;

    public BookRepository(CreateConnection connection) {
        this.connection = connection;
    }

    //create new book
    public boolean create(BookEntity book) {
        //add book to books table
        String sqlAddBook = """
                INSERT INTO books (isbn, title, price, publisher_id, category_id,total_quantity,
                book_status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (var bookSt = connection.getConnection().prepareStatement(sqlAddBook,
                Statement.RETURN_GENERATED_KEYS)) {
            bookSt.setString(1, book.getIsbn().getValue());
            bookSt.setString(2, book.getTitle());
            bookSt.setDouble(3, book.getPrice());
            bookSt.setInt(4, book.getPublisher_id());
            bookSt.setInt(5, book.getCategory_id());
            bookSt.setInt(6, book.getTotalQuantity());
            bookSt.setString(7, book.getBookStatus().toString());
            bookSt.executeUpdate();
            //get generated book id
            int bookId = 0;
            try (ResultSet rs = bookSt.getGeneratedKeys()) {
                if (rs.next())
                    bookId = rs.getInt(1);
            }

            //add to book_authors
            if (bookId > 0 && book.getAuthorIds() != null) {
                addBookToBookAuthors(bookId, book.getAuthorIds());
            } else return false;
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all books
    public List<BooksListDTO> retrieve() {
        String sql = """
                SELECT * FROM books
                JOIN publishers ON books.publisher_id=publishers.publisher_id
                JOIN categories ON books.category_id=categories.category_id;
                """;
        List<BooksListDTO> books = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet r = statement.executeQuery();) {
            while (r.next()) {
                books.add(new BooksListDTO(r.getInt("book_id"),
                        r.getString("isbn"), r.getString("title"),
                        r.getDouble("price"),
                        r.getString("publisher_name"), r.getString("category_name"),
                        r.getInt("total_quantity"), r.getString("book_status")));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //remove a book
    public boolean remove(int id) {
        String sql = "DELETE FROM books WHERE book_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            //remove book from book_authors table
            boolean isDeleted = removeBookFromBookAuthors(id);
            //then remove book
            statement.setInt(1, id);
            int rowAffected = statement.executeUpdate();

            return rowAffected > 0 && isDeleted;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //update a book
    public boolean update(BookEntity book) {
        String sql = """
                UPDATE books SET 
                isbn=?, title=?, price=?,
                publisher_id=?, category_id=?, total_quantity=?, book_status=?
                WHERE book_id=?
                """;

        try (var statement = connection.getConnection().prepareStatement(sql)) {
            statement.setString(1, book.getIsbn().getValue());
            statement.setString(2, book.getTitle());
            statement.setDouble(3, book.getPrice());
            statement.setInt(4, book.getPublisher_id());
            statement.setInt(5, book.getCategory_id());
            statement.setInt(6, book.getTotalQuantity());
            statement.setString(7, book.getBookStatus().toString());
            statement.setInt(8, book.getBookId());

            statement.executeUpdate();
            connection.getConnection().setAutoCommit(false);
            boolean isRemove = removeBookFromBookAuthors(book.getBookId());
            boolean isAdd = addBookToBookAuthors(book.getBookId(), book.getAuthorIds());
            connection.getConnection().commit();

            return isRemove & isAdd;
        } catch (SQLException e) {
            try {
                connection.getConnection().rollback();
            }catch (SQLException ex){}
            throw new RuntimeException(e.getMessage());
        }
    }

    //get book by id
    public BookDTO getBookById(int bookId) {
        String sql = """
                SELECT * FROM books
                JOIN publishers ON books.publisher_id=publishers.publisher_id 
                JOIN categories ON books.category_id=categories.category_id
                WHERE book_id=?
                """;
        try (var statement = connection.getConnection().prepareStatement(sql);) {
            List<Integer> authorIds = getAuthorsIds(bookId);

            statement.setInt(1, bookId);
            try (ResultSet r = statement.executeQuery())
            {
                if (r.next())
                    return new BookDTO(r.getInt("book_id"),
                            r.getString("isbn"),
                            r.getString("title"),
                            r.getDouble("price"),
                            r.getInt("publisher_id"),
                            r.getInt("category_id"),
                            r.getInt("total_quantity"),
                            authorIds,
                            r.getString("book_status"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all books of a publisher
    public List<BookListMiniDTO> getPublisherBookList(int publisherId) {
        String sql = """
                SELECT book_id, title, total_quantity, isbn 
                FROM books WHERE publisher_id = ?;
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, publisherId);

            List<BookListMiniDTO> books = new ArrayList<>();
            try (ResultSet r = statement.executeQuery()) {
                while (r.next())
                    books.add(new BookListMiniDTO(r.getInt("book_id"),
                            r.getString("title"), r.getString("isbn"),
                            r.getInt("total_quantity")));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all books of a category
    public List<BookListMiniDTO> getCategoryBookList(int categoryId) {
        String sql = """
                SELECT book_id, title, total_quantity, isbn 
                FROM books WHERE category_id = ?;
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, categoryId);

            List<BookListMiniDTO> books = new ArrayList<>();
            try (ResultSet r = statement.executeQuery()) {
                while (r.next())
                    books.add(new BookListMiniDTO(r.getInt("book_id"),
                            r.getString("title"), r.getString("isbn"),
                            r.getInt("total_quantity")));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all books of an author
    public List<BookListMiniDTO> getAllBooksOfAuthor(int authorId) {
        String sql = """
                SELECT b.book_id, b.title, b.isbn, b.total_quantity
                FROM books b
                JOIN book_authors ba ON b.book_id=ba.book_id
                JOIN authors a ON a.author_id=ba.author_id
                WHERE a.author_id=?; 
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)) {
            List<BookListMiniDTO> books = new ArrayList<>();
            statement.setInt(1, authorId);
            try (ResultSet r = statement.executeQuery()) {
                while (r.next())
                    books.add(new BookListMiniDTO(r.getInt("book_id"),
                            r.getString("title"), r.getString("isbn"),
                            r.getInt("total_quantity")));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private List<Integer> getAuthorsIds(int bookId){
        String sqlAuthorId = "SELECT author_id FROM book_authors WHERE book_id=?";
        List<Integer> authorIds = new ArrayList<>();
        try (var authorIdSt = connection.getConnection().prepareStatement(sqlAuthorId)){
            authorIdSt.setInt(1, bookId);
            try (ResultSet rs = authorIdSt.executeQuery();){
                while (rs.next()){
                    authorIds.add(rs.getInt("author_id"));
                }
            }
            return authorIds;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //------------------------------------
    private boolean removeBookFromBookAuthors(int bookId){
        String sqlRemove = "DELETE FROM book_authors WHERE book_id=?";

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sqlRemove)){
            statement.setInt(1, bookId);
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean addBookToBookAuthors(int bookId, List<Integer> authorIds) {
        String sqlAddBookAuthor = "INSERT INTO book_authors (author_id,book_id) VALUES (?,?)";
        try (var stmt = connection.getConnection().prepareStatement(sqlAddBookAuthor)) {
            for (int authorId : authorIds) {
                stmt.setInt(1, authorId);
                stmt.setInt(2, bookId);
                stmt.addBatch();
            }
            int[] rawAffected = stmt.executeBatch();
            return rawAffected.length > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //get authors of a book
    public List<String> getAllAuthorOfBook(int bookId){
        String sql = """
            SELECT a.author_name FROM authors a
            JOIN book_authors ba ON a.author_id=ba.author_id
            JOIN books b ON b.book_id=ba.book_id
            WHERE b.book_id=?
            """;
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql))
        {
            List<String> authors = new ArrayList<>();
            statement.setInt(1, bookId);
            try (ResultSet r = statement.executeQuery()){
                while (r.next())
                    authors.add(r.getString("author_name"));
            }
            return authors;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
