package com.library.repository.jdbc;

import com.library.domain.entity.AdminEntity;
import com.library.domain.entity.AdminUpdateEntity;
import com.library.domain.exception.DuplicateAdminUserNameException;
import com.library.infrastructure.persistance.CreateConnection;
import com.library.services.DTOs.AdminDTO;
import com.library.services.DTOs.AdminListDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {
    private final CreateConnection connection;

    public AdminRepository(CreateConnection connection){
        this.connection = connection;
    }

    //register new admin
    public boolean registerAdmin(AdminEntity admin){
        String sql = "INSERT INTO admins (full_name,username,password,admin_role) VALUES (?,?,?,?)";

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, admin.getFullName());
            statement.setString(2, admin.getUsername());
            statement.setString(3, admin.getPassword());
            statement.setString(4, admin.getAdminRole().toString());

            return statement.executeUpdate() > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //find password by username
    public String getPasswordByUsername(String username){
        String sql = "SELECT password FROM admins WHERE username=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, username);
            try (ResultSet r = statement.executeQuery();){
                if(r.next())
                    return r.getString("password");
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    //update
    public boolean updateAdmin(AdminUpdateEntity admin){
        String sql = """
                UPDATE admins SET
                full_name=?, username=?, admin_role=?
                WHERE admin_id=?
                """;
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1,admin.getFullName());
            statement.setString(2, admin.getUsername());
            statement.setString(3, admin.getAdminRole().toString());
            statement.setInt(4, admin.getAdminId());
            return statement.executeUpdate() > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //find if username exist
    public boolean isAdminUsernameExist(String username){
        String sql = "SELECT * FROM admins WHERE username=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, username);
            try (ResultSet r = statement.executeQuery()){
                if(r.next())
                    return r.getInt(1) > 0;
                else
                    return false;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //return admin by username
    public AdminDTO findAdminByID(int id){
        String sql = "SELECT admin_id,full_name,username,admin_role FROM admins WHERE admin_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql))
        {
            statement.setInt(1,id);
            try (ResultSet rs = statement.executeQuery()){
                if(rs.next())
                    return new AdminDTO(rs.getInt("admin_id"),
                            rs.getString("full_name"), rs.getString("username"),
                            "", rs.getString("admin_role"));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public AdminListDTO findAdminByUsername(String username){
        String sql = "SELECT *  FROM admins WHERE username=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1, username);
            try (ResultSet r = statement.executeQuery()){
                if(r.next())
                    return new AdminListDTO(r.getInt("admin_id"),
                            r.getString("full_name"),
                            r.getString("username"),
                            r.getString("admin_role"));
                else
                    return null;
            }
        }
        catch (SQLException e){
            throw  new RuntimeException(e.getMessage());
        }
    }

    //find admin by id

    //get all admins
    public List<AdminListDTO> getAllAdmins(){
        String sql = "SELECT admin_id,full_name,username,admin_role FROM admins";
        List<AdminListDTO> admins = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
                 ResultSet r = statement.executeQuery())
        {
            while (r.next()){
                var admin = new AdminListDTO(r.getInt("admin_id"),
                        r.getString("full_name"),
                        r.getString("username"),
                        r.getString("admin_role"));
                admins.add(admin);
            }
            return admins;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
