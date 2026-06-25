package com.library.repository.jdbc;

import com.library.domain.entity.PublisherEntity;
import com.library.services.DTOs.PublisherDTO;
import com.library.infrastructure.persistance.CreateConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherRepository{
    private final CreateConnection connection;

    public PublisherRepository(CreateConnection connection){
        this.connection = connection;
    }

    //create a publisher
    public boolean create(PublisherEntity publisher){
        String sql = "INSERT INTO publishers (publisher_name,publisher_url) VALUES (?,?)";

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, publisher.getPublisherName());
            statement.setString(2, publisher.getPublisherUrl());
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all publisher
    public List<PublisherDTO> retrieve(){
        String sql = "SELECT * FROM publishers";
        List<PublisherDTO> publishers = new ArrayList<>();

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet result = statement.executeQuery())
        {
            while (result.next())
                publishers.add(new PublisherDTO(result.getInt(1),
                        result.getString(2),
                        result.getString(3)));

            return publishers;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //remove publisher
    public boolean remove(int id){
        String sql = "DELETE FROM publishers WHERE publisher_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //update publisher
    public boolean update(PublisherEntity publisher){
        String sql = """
                UPDATE publishers 
                SET publisher_name=?, publisher_url=?
                WHERE publisher_id=?
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, publisher.getPublisherName());
            statement.setString(2, publisher.getPublisherUrl());
            statement.setInt(3, publisher.getPublisherId());

            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //find publisher by id
    public PublisherDTO findPublisherById(int id){
        String sql = "SELECT * FROM publishers WHERE publisher_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            try (ResultSet r = statement.executeQuery()){
                if(r.next())
                    return new PublisherDTO(r.getInt("publisher_id"),
                            r.getString("publisher_name"),
                            r.getString("publisher_url"));
                else
                    return null;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
