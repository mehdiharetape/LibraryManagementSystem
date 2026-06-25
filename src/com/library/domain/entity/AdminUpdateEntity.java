package com.library.domain.entity;

import com.library.domain.enums.AdminRole;
import com.library.domain.exception.AdminNotFoundException;
import com.library.domain.exception.ValidationException;

public class AdminUpdateEntity {
    private Integer adminId;
    private String fullName;
    private String username;
    private AdminRole adminRole;

    public AdminUpdateEntity(Integer adminId ,String fullName, String username,
                             AdminRole adminRole)
    {
        validation(adminId, fullName, username, adminRole);
        this.adminId = adminId;
        this.fullName = fullName;
        this.username = username;
        this.adminRole = adminRole;
    }

    public Integer getAdminId(){
        return adminId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public AdminRole getAdminRole(){
        return adminRole;
    }

    //validation
    private void validation(Integer adminId, String fullName, String username, AdminRole adminRole)
    {
        if(adminId != null && adminId <= 0)
            throw new AdminNotFoundException();
        if(fullName.length() < 5)
            throw new ValidationException("full name can't be less than 5 characters!!!");
        if(username.length() < 6)
            throw new ValidationException("username can't be less than 6 characters!!!");
        if(adminRole == null)
            throw new ValidationException("Admin role can't be empty");
    }
}
