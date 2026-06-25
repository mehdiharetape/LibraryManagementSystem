package com.library.mappers;

import com.library.domain.entity.MemberEntity;
import com.library.domain.valueobject.Email;
import com.library.domain.valueobject.Phone;
import com.library.services.DTOs.MemberDTO;

public class MemberMapper {
    public static MemberEntity toEntity(MemberDTO dto){
        MemberEntity entity = new MemberEntity();
        entity.setMemberId(dto.getId());
        entity.setFullName(dto.getFullName());
        entity.setNationalCode(dto.getNationalCode());
        entity.setBirthDate(dto.getBirthDate());
        entity.setAddress(dto.getAddress());
        entity.setEmail(new Email(dto.getEmail()));
        entity.setPhoneNumber(new Phone(dto.getPhoneNumber()));
        return entity;
    }
}
