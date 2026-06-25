package com.library.services;

import com.library.domain.entity.AdminEntity;
import com.library.domain.entity.AdminUpdateEntity;
import com.library.domain.enums.AdminRole;
import com.library.domain.exception.AdminNotFoundException;
import com.library.domain.exception.AuthorizationException;
import com.library.domain.exception.DuplicateAdminUserNameException;
import com.library.domain.exception.ValidationException;
import com.library.repository.jdbc.AdminRepository;
import com.library.services.DTOs.AdminDTO;
import com.library.services.DTOs.AdminListDTO;
import com.library.utils.PasswordHasher;
import com.library.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    //register admin
    public boolean registerAdmin(AdminEntity admin){
        requireSuperAdmin();
        if(adminRepository.isAdminUsernameExist(admin.getUsername()))
            throw new DuplicateAdminUserNameException();
        String securedPassword = PasswordHasher.createNewHash(admin.getPassword());
        admin.setPassword(securedPassword);
        return adminRepository.registerAdmin(admin);
    }

    //admin update
    public boolean updateAminService(AdminUpdateEntity admin){
        requireSuperAdmin();
        var founded = adminRepository.findAdminByID(admin.getAdminId());
        if(founded == null)
            throw new AuthorizationException("Admin not found !");
        if(!founded.getFullName().equals(admin.getFullName())
            && adminRepository.isAdminUsernameExist(admin.getUsername()))
        {
            throw new DuplicateAdminUserNameException();
        }
        return adminRepository.updateAdmin(admin);
    }

    //login admin
    public boolean loginAdmin(String username, String plainPassword){
        String storedPassWithSalt = adminRepository.getPasswordByUsername(username);
        if(storedPassWithSalt == null)
            throw new AuthorizationException("Can't find this username!!!");
        return PasswordHasher.verifyPassword(plainPassword, storedPassWithSalt);
    }

    //get all admins
    public List<AdminListDTO> getAllAdminService(){
        var admins = adminRepository.getAllAdmins();
        if(admins == null)
            throw new AuthorizationException("There is no admin");
        return admins;
    }

    //get admin by id
    public AdminDTO getAdminByIdService(int adminId){
        if(adminId <= 0)
            throw new AdminNotFoundException();
        return adminRepository.findAdminByID(adminId);
    }

    public AdminListDTO getAdminByUsername(String username){
        if(username == null || username.isEmpty())
            throw new AdminNotFoundException();
        return adminRepository.findAdminByUsername(username);
    }

    //check super admin
    private void requireSuperAdmin(){
        if(!Session.isLoggedIn() ||
                !Session.get().getAdminRole().equals(AdminRole.SUPER_ADMIN.toString()))
        {
            throw new AuthorizationException("Access Denied!!!");
        }
    }
}
