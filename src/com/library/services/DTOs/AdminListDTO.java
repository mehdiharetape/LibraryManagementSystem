package com.library.services.DTOs;

public class AdminListDTO {
    private int adminId;
    private String adminName;
    private String adminUserName;
    private String adminRole;

    public AdminListDTO(int adminId, String adminName, String adminUserName, String adminRole)
    {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminUserName = adminUserName;
        this.adminRole = adminRole;
    }

    public String getAdminUserName() {
        return adminUserName;
    }

    public String getAdminName() {
        return adminName;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getAdminRole(){
        return adminRole;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
}
