package com.library.repository.jdbc;

import com.library.infrastructure.persistance.CreateConnection;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.BookSearchDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchBookRepository {
    private final CreateConnection connection;

    public SearchBookRepository(CreateConnection connection){
        this.connection = connection;
    }

    //search by title, author and category
    public List<BookSearchDTO> searchBook(String keyWord){
        String sql = """
                SELECT DISTINCT b.*
                FROM books b 
                LEFT JOIN book_authors ba ON b.book_id=ba.book_id 
                LEFT JOIN authors a ON a.author_id=ba.author_id 
                LEFT JOIN categories c ON b.category_id=c.category_id
                WHERE b.title LIKE ?
                OR a.author_name LIKE ?
                OR category_name LIKE ?
                """;
        List<BookSearchDTO> foundBooks = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            String searchPattern = "%" + keyWord + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try (ResultSet r = statement.executeQuery()){
                while (r.next())
                    foundBooks.add(new BookSearchDTO(r.getInt("book_id"),
                            r.getString("isbn"), r.getString("title"),
                            r.getDouble("price"),
                            r.getInt("total_quantity")));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return foundBooks;
    }
}
