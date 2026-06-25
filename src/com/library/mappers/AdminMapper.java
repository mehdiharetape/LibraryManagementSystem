package com.library.mappers;

import com.library.domain.entity.AdminEntity;
import com.library.domain.entity.AdminUpdateEntity;
import com.library.domain.enums.AdminRole;
import com.library.services.DTOs.AdminDTO;

public class AdminMapper {
    public static AdminEntity toEntity(AdminDTO dto){
        AdminEntity entity = new AdminEntity();
        entity.setFullName(dto.getFullName());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setAdminRole(AdminRole.valueOf(dto.getAdminRole()));
        return entity;
    }

    public static AdminUpdateEntity toUpdateEntity(AdminDTO dto){
        AdminUpdateEntity updateEntity = new AdminUpdateEntity(
                dto.getAdminId(),
                dto.getFullName(),
                dto.getUsername(),
                AdminRole.valueOf(dto.getAdminRole())
        );
        return updateEntity;
    }
}
