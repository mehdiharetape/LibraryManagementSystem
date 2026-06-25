package com.library.services.DTOs;

import com.library.domain.entity.Model;

public class AdminDTO implements Model {
    private Integer adminId;
    private String fullName;
    private String username;
    private String password;
    private String adminRole;

    public AdminDTO(Integer adminId,String fullName, String username,
                    String password, String adminRole){
        this.adminId = adminId;
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

    public Integer getAdminId(){
        return adminId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAdminRole(){return adminRole;}
}
