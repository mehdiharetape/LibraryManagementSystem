package com.library.conroller;

import com.library.domain.entity.AdminEntity;
import com.library.domain.entity.AdminUpdateEntity;
import com.library.domain.enums.AdminRole;
import com.library.mappers.AdminMapper;
import com.library.services.AdminService;
import com.library.services.DTOs.AdminDTO;
import com.library.services.DTOs.AdminListDTO;

import java.util.List;

public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    //register admin
    public boolean handlerRegisterAdmin(AdminDTO adminDTO){
        AdminEntity admin = AdminMapper.toEntity(adminDTO);
        return adminService.registerAdmin(admin);
    }

    //update admin
    public boolean handleUpdateAdmin(AdminDTO adminDTO){
        var admin = AdminMapper.toUpdateEntity(adminDTO);
        return adminService.updateAminService(admin);
    }

    //login admin
    public boolean handlerLoginAdmin(String username, String plainPassword){
        return adminService.loginAdmin(username, plainPassword);
    }

    //all admins
    public List<AdminListDTO> handleGetAllAdmins(){
        return adminService.getAllAdminService();
    }

    public AdminDTO handleGetAdminById(int adminId){
        return adminService.getAdminByIdService(adminId);
    }

    //find admin by username
    public AdminListDTO handleFindAdminByUsername(String username){
        return adminService.getAdminByUsername(username);
    }
}
