package com.library.domain.entity;

import com.library.domain.enums.AdminRole;
import com.library.domain.exception.AdminNotFoundException;
import com.library.domain.exception.ValidationException;

public class AdminEntity implements Model {
    private String fullName;
    private String username;
    private String password;
    private AdminRole adminRole;

    public AdminEntity(){}

    public AdminEntity(String fullName, String username,
                       String password, AdminRole adminRole)
    {
        validation(fullName, username, password, adminRole);
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.adminRole = adminRole;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public AdminRole getAdminRole(){
        return adminRole;
    }

    //setters


    public void setFullName(String fullName) {
        if(fullName.length() < 5)
            throw new ValidationException("full name can't be less than 5 characters!!!");
        this.fullName = fullName;
    }

    public void setUsername(String username) {
        if(username.length() < 6)
            throw new ValidationException("username can't be less than 6 characters!!!");
        this.username = username;
    }

    public void setPassword(String password) {
        if(password.length() < 6)
            throw new ValidationException("password can't be less than 6 characters!!!");
        this.password = password;
    }

    public void setAdminRole(AdminRole adminRole) {
        if(adminRole == null)
            throw new ValidationException("Admin role can't be empty");
        this.adminRole = adminRole;
    }

    //validation
    private void validation(String fullName, String username,
                            String password, AdminRole adminRole)
    {
        if(fullName.length() < 5)
            throw new ValidationException("full name can't be less than 5 characters!!!");
        if(username.length() < 6)
            throw new ValidationException("username can't be less than 6 characters!!!");
        if(password.length() < 6)
            throw new ValidationException("password can't be less than 6 characters!!!");
        if(adminRole == null)
            throw new ValidationException("Admin role can't be empty");
    }
}
