package com.library.repository.jdbc;

import com.library.domain.entity.AuthorEntity;
import com.library.services.DTOs.AuthorDTO;
import com.library.infrastructure.persistance.CreateConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    private final CreateConnection connection;

    public AuthorRepository(CreateConnection connection){
        this.connection = connection;
    }

    //create author
    public boolean create(AuthorEntity author){
        String sql = "INSERT INTO authors (author_name,author_url) VALUES (?,?)";

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, author.getAuthorName());
            statement.setString(2, author.getAuthorUrl());
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all authors
    public List<AuthorDTO> retrieve(){
        String sql = "SELECT * FROM authors";
        List<AuthorDTO> authors = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet r = statement.executeQuery();)
        {
            while (r.next())
                authors.add(new AuthorDTO(r.getInt("author_id"),
                        r.getString("author_name"), r.getString("author_url")));

            return authors;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //remove author
    public boolean remove(int id){
        String sql = "DELETE FROM authors WHERE author_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //update author
    public boolean update(AuthorEntity author){
        String sql = """
                UPDATE authors
                SET author_name=?, author_url=?
                WHERE author_id=?
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql))
        {
            statement.setString(1, author.getAuthorName());
            statement.setString(2, author.getAuthorUrl());
            statement.setInt(3, author.getAuthorId());

            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //find author by id
    public AuthorDTO findAuthorById(int id){
        String sql = "SELECT * FROM authors WHERE author_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            try (ResultSet r = statement.executeQuery()){
                if(r.next())
                    return new AuthorDTO(r.getInt("author_id"),
                            r.getString("author_name"),
                            r.getString("author_url"));
                else
                    return null;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
