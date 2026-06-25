package com.library.repository.jdbc;

import com.library.domain.entity.CategoryEntity;
import com.library.services.DTOs.CategoryDTO;
import com.library.infrastructure.persistance.CreateConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository{
    private final CreateConnection connection;

    public CategoryRepository(CreateConnection connection){
        this.connection = connection;
    }

    //create new category
    public boolean create(CategoryEntity category) {
        String sql = "INSERT INTO categories (category_name) VALUES (?)";

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, category.getCategoryName());
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //retrieve category lists
    public List<CategoryDTO> retrieve(){
        String sql = "SELECT * FROM categories";
        List<CategoryDTO> categories = new ArrayList<>();

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet result = statement.executeQuery()){
            while (result.next())
                categories.add(new CategoryDTO(
                        result.getInt(1) ,result.getString(2)));

            return categories;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //remove a category
    public boolean remove(int id){
        String sql = "DELETE FROM categories WHERE category_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1,id);
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //update category
    public boolean update(CategoryEntity category){
        String sql = "UPDATE categories SET category_name=? WHERE category_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, category.getCategoryName());
            statement.setInt(2, category.getCategoryId());
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //is category exists
    public boolean isCategoryExists(int categoryId){
        String sql = "SELECT category_id FROM categories WHERE category_id=?";

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, categoryId);
            try (ResultSet r = statement.executeQuery()){
                if (r.next())
                    return true;
            }
            return false;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get category by id
    public CategoryDTO getCategoryById(int categoryId){
        String sql = "SELECT * FROM categories WHERE category_id=?";

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, categoryId);
            try (ResultSet r = statement.executeQuery()){
                if (r.next())
                    return new CategoryDTO(r.getInt("category_id"),
                            r.getString("category_name"));
            }
            return null;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
