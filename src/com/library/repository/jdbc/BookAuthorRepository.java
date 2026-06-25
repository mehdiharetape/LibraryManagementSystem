package com.library.repository.jdbc;

import com.library.domain.entity.BookAuthorEntity;
import com.library.services.DTOs.BookAuthorDTO;
import com.library.infrastructure.persistance.CreateConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookAuthorRepository {

    private final CreateConnection connection;
    public BookAuthorRepository(CreateConnection connection){
        this.connection = connection;
    }

    //create book author
    public boolean create(BookAuthorEntity bookAuthor){
        String sql = "INSERT INTO book_authors (author_id,book_id) VALUES (?,?)";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, bookAuthor.getAuthorId());
            statement.setInt(2, bookAuthor.getBookIk());
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all book-authors
    public List<BookAuthorDTO> retrieve(){
        String sql = """
                SELECT book_authors.id, authors.author_name, books.title
                FROM book_authors
                JOIN authors ON book_authors.author_id=authors.author_id
                JOIN books ON book_authors.book_id=books.book_id 
                """;

        List<BookAuthorDTO> bookAuthors = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet r = statement.executeQuery())
        {
            while (r.next())
                bookAuthors.add(new BookAuthorDTO(r.getInt("id"),
                        r.getString("author_name"), r.getString("title")));
            return bookAuthors;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //remove
    public boolean remove(int id){
        String sql = "DELETE FROM book_authors WHERE id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //update
    public boolean update(int id ,int authorId, int bookId){
        String sql = "UPDATE book_authors SET author_id=?,book_id=? WHERE id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, authorId);
            statement.setInt(2, bookId);
            statement.setInt(3, id);

            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get bookAuthor by id

}
